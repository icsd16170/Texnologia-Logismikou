package project.service.authentication;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import project.dto.UserAction;
import project.dto.UserDTO;
import project.errorhandling.exception.AuthenticationFailedException;
import project.errorhandling.exception.TokenVerificationFailedException;
import project.errorhandling.exception.VerificationRoleException;
import project.persistence.entity.TokenEntity;
import project.service.Constants;
import project.service.encryption.EncryptionService;
import project.service.token.TokenService;
import project.service.user.UserService;

/**
 * Einai h klash h opoia diaxeirizetai tin authentikopihsh tou xrhsth (login), thn epikurwsh tou token, kai thn epikurwsh ton rolwn tou enos xrhsth
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    public static final String TOKEN_VERIFICATION = "TOKEN_VERIFICATION"; //o xrhsths ekane verify token

    private static final String AUTHENTICATION = "AUTHENTICATION"; //o xrhsths ekane login

    private static final String VERIFY_OLD_PASSWORD = "VERIFY_OLD_PASSWORD";  //o xrhsths prospathise na allaksei kwdiko kai ginetai epivevaiwsh tou paliou

    private final TokenService tokenService;

    private final EncryptionService encryptionService;

    // map to opoio krataei poia einai h teleutaia energeia tou kathe xrhsth kai poses fores tin exei kanei
    private static final Map<String, UserAction> userActionMap = new HashMap<>();

    private final UserService userService;

    public AuthenticationServiceImpl(TokenService tokenService, EncryptionService encryptionService,
            UserService userService) {
        this.tokenService = tokenService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    /**
     * H methodos auth einai upeuthini gia to login
     *
     * @param userName to userName tou xrhsth
     * @param password o kwdikos tou xrhsth
     * @return String token
     */
    @Override
    public String authenticateUser(String userName, String password) {
        //tsekare an einai h prwth fora pou o xrhsths kanei login. An nai vale sto map me tis energeies oti exei kanei AUTHENTICATION 0 fores
        initializeUserAction(userName, AUTHENTICATION);
        /*
        vres to xrhsth. An einai anenergos tote peta exception alliws tsekare an tairiazoun oi kwdikoi
        diegrapse to token pou eixe prin (an eixe), ftiakse kainourgio, save kai gurna to
         */
        UserDTO user = userService.findByUserName(userName);
        if (!user.isActive()) {
            throw new AuthenticationFailedException("User is inactive");
        } else if (encryptionService.stringsMatch(user.getPassword(), password)) {
            tokenService.deleteTokenForUser(userName);
            TokenEntity tokenEntity = tokenService.createTokenForUser(userName, password);
            tokenService.save(tokenEntity);
            userActionMap.put(userName, new UserAction(AUTHENTICATION, 0));
            return tokenEntity.getTokenCode();
        }

        /*
        edw ftanei mono otan exei kanei lathos kwdiko. Pare tin teleutaia energeia pou ekane o xrhsths. Tsekare an htane login
        . An nai anevase thn prospatheia gia login +1. An einai h trith fora kane deactivate ton xrhsth kai peta exception
         */
        UserAction userAction = userActionMap.get(userName);
        if (AUTHENTICATION.equals(userAction.getLastApiCalled())) {
            int tries = userAction.getTries() + 1;
            userAction.setTries(tries);
            userActionMap.put(userName, userAction);
            if (tries == 3) {
                userService.deactivateUser(userName);
                tokenService.deleteTokenForUser(userName);
            }
        }
        throw new AuthenticationFailedException("Invalid Credentials");
    }



    /**
     * Auth h methodos einai upethunei gia thn epikurwsh tou token.
     *
     * @param tokenCode    tokenCode pou dinei o xrhsths
     * @param loggedInUser to username tou xrhsth pou exei kanei log in sto susthma (authentication step)
     * @return an einai egkuro to token gurnaei pisw to rolo tou logged in xrhsth
     */
    @Override
    public String verifyToken(String tokenCode, String loggedInUser) {
        //krata thn prohgoumenh energeia pou exei kanei o xrhsths prin to token verification
        UserAction previousUserAction = userActionMap.get(loggedInUser);

        //tsekare an einai h prwth fora pou o xrhsths kanei epikurwsh token. An nai vale sto map me tis energeies oti exei kanei TOKEN_VERIFICATION 0 fores
        initializeUserAction(loggedInUser, TOKEN_VERIFICATION);
        //prospathise na vreis to token sth vash. An den uparxei to token, an exei leiksei, h anoikei se allo xrhsth tote vale setare to
        //verificationPassed se false
        Optional<TokenEntity> tokenOptional = tokenService.findByTokenCode(tokenCode);
        boolean verificationPassed = true;
        String exceptionMessage = "";

        //an exei vrethei token me auto to code kai den einai egkuro h anoikei se allo xrhsth
        if (tokenOptional.isPresent()) {
            TokenEntity token = tokenOptional.get();
            if (token.getValidTo().isBefore(LocalDateTime.now())) {
                exceptionMessage += "Token has expired.";
                verificationPassed = false;
            }
            if (!loggedInUser.equals(token.getUserName())) {
                exceptionMessage += "Token does not belong to user";
                verificationPassed = false;
            }
        } else {
            verificationPassed = false;
            exceptionMessage += "Invalid token.";
        }

         /*
        edw ftanei mono otan den exei petuxei to verifiation. Pare tin teleutaia energeia pou ekane o xrhsths. Tsekare an htane epikurwsh token
        . An nai anevase thn prospatheia gia epikurwsh +1. An einai h trith fora kane deactivate ton xrhsth kai peta exception
         */
        if (!verificationPassed) {
            UserAction userAction = userActionMap.get(loggedInUser);
            if (TOKEN_VERIFICATION.equals(userAction.getLastApiCalled())) {
                int tries = userAction.getTries() + 1;
                userAction.setTries(tries);
                userActionMap.put(loggedInUser, userAction);
                if (tries == 3) {
                    userService.deactivateUser(loggedInUser);
                    tokenOptional.ifPresent(token -> {
                        userService.deactivateUser(token.getUserName());
                        tokenService.deleteTokenForUser(token.getUserName());
                    });
                    tokenService.deleteTokenForUser(loggedInUser);
                }
            }

            throw new TokenVerificationFailedException(exceptionMessage);
        } else {
            /*
            an exei petuxei to verification tsekare an h prohgoumenh energeia tou xrhsth pou eswses sthn arxh htane
            na epikurwsei palio kwdiko. An nai ksana vale stan prohgoumenh energeia oti phge na allaksei kwdiko + tis fores pou eixe.
            auto ginetai giati sth periptwsh pou propathisei na allaksei kapoios kwdiko prwta ginetai token verification kai meta
            old password verification opote an de setareis san prohgoumenh energeia thn epikurwsh paliou kwdikou tote xanetai
            poses fores exei prospathisei na allaksei kwdiko kai etsi de tha blockaristei pote
             */
            userActionMap.put(loggedInUser, new UserAction(TOKEN_VERIFICATION, 0));
            if (VERIFY_OLD_PASSWORD.equals(previousUserAction.getLastApiCalled())) {
                userActionMap.put(loggedInUser, previousUserAction);
            }
            return userService.findByUserName(loggedInUser).getRole();
        }
    }

    /**
     * Tsekarei an kapoios kanei mia energeia sto onoma kapoiou allou, (p.x o upallhlos na kanei paragellia gia enan pelath) exei ton rolo pou prepei
     *
     * @param loggedInUserRole o rolos tou atomou pou prospathei na kanei mia energeia
     * @param userName         to atomo gia to opoio ginetai h energeia
     * @param loggedInUserName to atomo pou kanei thn energeia
     * @param role             o rolos pou prepei na exei to atomo pou kanei thn ernergeia
     * @return true/false
     */
    @Override
    public boolean verifyRole(String loggedInUserRole, String userName, String loggedInUserName, String role) {
        if (!loggedInUserName.equals(userName) && !role.equals(loggedInUserRole)) {
            throw new VerificationRoleException();
        }
        return true;
    }

    /**
     * tsekarei an to atomo pou kanei kapoia energeia einai o ADMIN
     *
     * @param loggedInRole o rolos tou atomou pou kanei thn energeia
     * @return true/false
     */
    @Override
    public boolean verifyAdminRole(String loggedInRole) {
        if (!Constants.ADMIN.equals(loggedInRole)) {
            throw new VerificationRoleException();
        }
        return true;
    }

    /**
     * auth h methodos kaleitai otan o xrhsths prospathei na allaksei kwdiko etsi wste na epivevaiwsei ton palio pou dinetai ws eisodos alliws petaei
     * AuthenticationFailedException Se kathe periptwsh ta token tou xrhsth diagrafontai
     *
     * @param oldPassword o palios kwdikos
     * @param username    to username tou atomou pou prospathei na allaksei kwdiko
     */
    @Override
    public void verifyOldPassword(String oldPassword, String username) {
        //tsekare an einai h prwth fora pou o xrhsths prwspathei na allaksei kwdiko. An nai vale sto map me tis energeies oti exei kanei VERIFY_OLD_PASSWORD 0 fores
        initializeUserAction(username, VERIFY_OLD_PASSWORD);
        UserDTO user = userService.findByUserName(username); //vres to xrhsth

        //tsekare an tairiazoune oi kwdikoi
        if (!encryptionService.stringsMatch(user.getPassword(), oldPassword)) {
            UserAction userAction = userActionMap.get(username);
               /*
        edw ftanei mono otan exei kanei lathos kwdiko. Pare tin teleutaia energeia pou ekane o xrhsths. Tsekare an htane old password verification
        . An nai anevase thn prospatheia gia old password verification +1. An einai h trith fora kane deactivate ton xrhsth kai diegrapse ta token (logout)
         */
            if (VERIFY_OLD_PASSWORD.equals(userAction.getLastApiCalled())) {
                int tries = userAction.getTries() + 1;
                userAction.setTries(tries);
                userActionMap.put(username, userAction);
                if (tries == 3) {
                    userService.deactivateUser(username);
                    tokenService.deleteTokenForUser(username);
                }
            }

            throw new AuthenticationFailedException("Password is invalid");
        }

        userActionMap.put(username, new UserAction(VERIFY_OLD_PASSWORD, 0));

        //se periptwsh pou petuxei diegrapse to token tou xrhsth (logout)
        tokenService.deleteTokenForUser(username);
    }


    private void initializeUserAction(String userName, String action) {
        if (userActionMap.get(userName) == null || !action.equals(
                userActionMap.get(userName).getLastApiCalled())) {
            userActionMap.put(userName, new UserAction(action, 0));
        }
    }


    /**
     * tsekarei an to atomo pou kanei kapoia energeia einai o EMPLOYEE
     *
     * @param loggedInRole o rolos tou atomou pou kanei thn energeia
     * @return true/false
     */
    @Override
    public boolean verifyEmployeeRole(String loggedInRole) {
        if (!Constants.EMPLOYEE.equals(loggedInRole)) {
            throw new VerificationRoleException();
        }
        return true;
    }
}
