package com.example.onjeong.profile.service;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.profile.domain.CoinHistory;
import com.example.onjeong.profile.domain.CoinHistoryType;
import com.example.onjeong.profile.domain.Flower;
import com.example.onjeong.profile.domain.FlowerKind;
import com.example.onjeong.profile.dto.CoinHistoryDto;
import com.example.onjeong.profile.repository.FlowerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;
    private final UserRepository userRepository;

    public FlowerKind showFlower(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sendUser = userRepository.findByUserNickname(authentication.getName());
        Family family = sendUser.get().getFamily();

        Flower flower = flowerRepository.findBlooming(family.getFamilyId());
        return flower.getFlowerKind();

    }

    public List<FlowerKind> showFlowerBloom(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sendUser = userRepository.findByUserNickname(authentication.getName());
        Family family = sendUser.get().getFamily();

        List<Flower> flower = flowerRepository.findFullBloom(family.getFamilyId());
        List<FlowerKind> bloomFlower = new ArrayList<>();

        for(Flower f : flower){
            System.out.println(f.getFlowerId());
            bloomFlower.add(f.getFlowerKind());
        }
        return bloomFlower;

    }
}
