package com.app.toaster.service.link;

import com.app.toaster.controller.response.toast.WeekLinkDto;
import com.app.toaster.infrastructure.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    public List<WeekLinkDto> getWeekLinks(){

        return linkRepository.findRandom3Links().stream().map(WeekLinkDto::of).toList();
    }



}
