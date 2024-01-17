package com.app.toaster.service.link;

import com.app.toaster.controller.response.toast.WeekSiteDto;
import com.app.toaster.infrastructure.LinkRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    public List<WeekSiteDto> getWeekLinks(){

        return linkRepository.findRandom3Links().stream().map(WeekSiteDto::of).toList();
    }



}
