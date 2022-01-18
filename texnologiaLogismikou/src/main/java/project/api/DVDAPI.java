package project.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.DVDDTO;
import project.dto.DVDStatisticsDTO;

@RequestMapping("/dvd-managmement")
@Tag(name = "DVDs")
public interface DVDAPI {


    @PostMapping("/dvds/create")
    ResponseEntity<DVDDTO> create(@Valid @RequestBody DVDDTO dvdDTO, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);

    @GetMapping("dvds/{id}")
    ResponseEntity<DVDDTO> findById(Long id, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);

    @GetMapping("dvds")
    ResponseEntity<List<DVDDTO>> findByAll(@RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);

    @DeleteMapping("{id}/delete")
    ResponseEntity<String> delete(@PathVariable("id") Long id, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);


    @PutMapping("/dvds/{id}")
    ResponseEntity<DVDDTO> update(@PathVariable("id") Long id, @RequestBody DVDDTO dvdDTO, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);


    @GetMapping("dvds/{id}/statistics")
    ResponseEntity<DVDStatisticsDTO> getDVDStatistics(Long id, @RequestHeader("loggedInUserName") String loggedInUserName,
            @RequestHeader("token") String token);
}

