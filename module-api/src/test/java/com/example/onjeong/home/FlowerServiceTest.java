package com.example.onjeong.home;

import com.example.onjeong.family.domain.Family;
import com.example.onjeong.home.domain.Flower;
import com.example.onjeong.home.dto.FlowerDto;
import com.example.onjeong.home.repository.FlowerRepository;
import com.example.onjeong.home.service.HomeService;
import com.example.onjeong.user.domain.User;
import com.example.onjeong.util.AuthUtil;
import com.example.onjeong.util.FamilyUtils;
import com.example.onjeong.util.FlowerUtils;
import com.example.onjeong.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlowerServiceTest {

    @InjectMocks
    private HomeService homeService;

    @Mock
    private FlowerRepository flowerRepository;

    @Mock
    private AuthUtil authUtil;

    @Test
    void 현재키우는꽃확인() {
        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);
        final Flower flower = FlowerUtils.getRandomFlower(family, 1, false);

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(flower).when(flowerRepository).findBlooming(family.getFamilyId());

        //when
        homeService.showFlower();

        //then
        verify(flowerRepository,times(1)).findBlooming(family.getFamilyId());
    }

    @Test
    void 만개한꽃장확인() {

        //given
        final Family family = FamilyUtils.getRandomFamily();
        final User user = UserUtils.getRandomUser(family);

        List<Flower> flowerList = new ArrayList<>();
        for(int i=0; i<3; i++){
            flowerList.add(FlowerUtils.getRandomFlower(family, 1, true));
        }

        doReturn(user).when(authUtil).getUserByAuthentication();
        doReturn(flowerList).when(flowerRepository).findFullBloom(family.getFamilyId());

        //when
        List<FlowerDto> flowerDtoList = homeService.showFlowerBloom();

        //then
        for(int i=0; i<3; i++){
            assertNotNull(flowerDtoList.get(i).getFlowerBloomDate());
        }

    }
}