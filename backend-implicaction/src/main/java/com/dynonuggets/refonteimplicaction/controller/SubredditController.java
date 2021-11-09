package com.dynonuggets.refonteimplicaction.controller;

import com.dynonuggets.refonteimplicaction.dto.SubredditDto;
import com.dynonuggets.refonteimplicaction.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dynonuggets.refonteimplicaction.utils.ApiUrls.GET_ALL_BY_TOP_POSTING_URI;
import static com.dynonuggets.refonteimplicaction.utils.ApiUrls.SUBREDDITS_BASE_URI;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(SUBREDDITS_BASE_URI)
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        final SubredditDto saveDto = subredditService.save(subredditDto);
        return ResponseEntity.status(CREATED).body(saveDto);
    }

    @GetMapping
    public ResponseEntity<Page<SubredditDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "rows", defaultValue = "10") int rows,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {
        Pageable pageable = PageRequest.of(page, rows, Sort.by(Sort.Direction.valueOf(sortOrder), sortBy));
        Page<SubredditDto> subredditDtos = subredditService.getAll(pageable);
        return ResponseEntity.ok(subredditDtos);
    }

    @GetMapping(GET_ALL_BY_TOP_POSTING_URI)
    public ResponseEntity<List<SubredditDto>> getAllByTopPosting(@RequestParam int limit) {
        List<SubredditDto> subredditDtos = subredditService.getAllByTopPosting(limit);
        return ResponseEntity.ok(subredditDtos);
    }
}
