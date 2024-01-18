package com.app.toaster.service.link;

import com.app.toaster.controller.response.toast.WeekLinkDto;
import com.app.toaster.domain.Link;
import com.app.toaster.infrastructure.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    @Transactional
    public List<WeekLinkDto> getWeekLinks(){

        List<Link> lists = linkRepository.findAllByThisWeekLinkIsTrue();

        long days = Duration.between(lists.get(0).getUpdateAt(), LocalDateTime.now()).toDays();

        if(days>7){
            lists.forEach(Link::setWeekLinkFalse);
            lists = linkRepository.findRandom3Links().stream()
                    .peek(Link::setUpdateAtNow)
                    .peek(Link::setWeekLinkTrue)
                    .toList();
        }


        return lists.stream().map(WeekLinkDto::of).toList();
    }

}
