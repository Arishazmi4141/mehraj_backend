package e_commerce.khilat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.entity.Magazine;
import e_commerce.khilat.service.MagazineService;

@RestController
@RequestMapping("/api/magazines")
@CrossOrigin
public class MagazineController {

    @Autowired
    private MagazineService magazineService;

    @GetMapping
    public ResponseEntity<Page<Magazine>> getAllMagazines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(magazineService.getAllMagazines(pageable));
    }
}