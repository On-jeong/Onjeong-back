package com.example.onjeong.home.service;

import com.example.onjeong.error.ErrorCode;
import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.domain.FlowerKind;
import com.example.onjeong.home.dto.FlowerDto;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.user.exception.UserNotExistException;
import com.example.onjeong.user.repository.UserRepository;
import com.example.onjeong.util.AuthUtil;
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
    private final AuthUtil authUtil;

    public FlowerDto showFlower(){

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        Flower flower = flowerRepository.findBlooming(family.getFamilyId());
        FlowerDto flowerDto = FlowerDto.builder()
                .flowerLevel(flower.getFlowerLevel())
                .flowerKind(flower.getFlowerKind())
                .flowerColor(flower.getFlowerColor())
                .build();
        return flowerDto;

    }

    public List<FlowerDto> showFlowerBloom(){

        User user = authUtil.getUserByAuthentication();
        Family family = user.getFamily();

        List<Flower> flower = flowerRepository.findFullBloom(family.getFamilyId());
        List<FlowerDto> bloomFlower = new ArrayList<>();

        for(Flower f : flower){
            FlowerDto flowerDto = FlowerDto.builder()
                    .flowerBloomDate(f.getFlowerBloomDate())
                    .flowerColor(f.getFlowerColor())
                    .flowerKind(f.getFlowerKind())
                    .build();

            bloomFlower.add(flowerDto);
        }
        return bloomFlower;

    }
}
