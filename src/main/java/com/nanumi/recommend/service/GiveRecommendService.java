package com.nanumi.recommend.service;

import com.nanumi.board_give.DataNotFoundException.DataNotFoundException;
import com.nanumi.board_give.dto.BoardDTO;
import com.nanumi.board_give.entity.BoardEntity;
import com.nanumi.board_give.entity.UsersLikesEntity;
import com.nanumi.board_give.repository.BoardRepository;
import com.nanumi.board_give.repository.UsersLikesRepository;
import com.nanumi.recommend.entity.FinalDistributionEntity;
import com.nanumi.recommend.entity.UserViewLogEntity;
import com.nanumi.recommend.repository.FinalDistributionRepository;
import com.nanumi.recommend.repository.UserViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiveRecommendService {
    private final UserViewLogRepository userViewLogRepository;
    private final BoardRepository boardRepository;
    private final UsersLikesRepository userLikeRepository;
    private final FinalDistributionRepository finalDistributionRepository;

    static int categorynum = 2;
    // 새로운 카테고리가 추가될 경우 여기에 추가하면 된다
    static List<String> categories = Arrays.asList("코딩", "소설");

    // 사용자 조회 글 카테고리 기록
    public void saveviewlog(String user_seq, Long board_give_id){ // 사용자 조회 글 카테고리 기록

        Optional<BoardEntity> boardGiveOptional = boardRepository.findById(board_give_id);

        if (boardGiveOptional.isPresent()) {
            BoardEntity boardGiveEntity = boardGiveOptional.get();
            String categoryId = boardGiveEntity.getCategory_id();

            UserViewLogEntity userViewLogEntity = userViewLogRepository.findByUserSeq(user_seq);

            if (userViewLogEntity == null) {
                userViewLogEntity = new UserViewLogEntity();
                userViewLogEntity.setUser_seq(user_seq);
                userViewLogEntity.setView_cnt_category1(0.0f);
                userViewLogEntity.setView_cnt_category2(0.0f);

                switch (categoryId) {
                    case "코딩":
                        userViewLogEntity.setView_cnt_category1(1.0f);
                        break;
                    case "소설":
                        userViewLogEntity.setView_cnt_category2(1.0f);
                        break;
                }
            } else {
                switch (categoryId){
                    case "코딩":
                        Float currentCnt1 = userViewLogEntity.getView_cnt_category1();
                        userViewLogEntity.setView_cnt_category1(currentCnt1 != null ? currentCnt1 + 1 : 1);
                        break;
                    case "소설":
                        Float currentCnt2 = userViewLogEntity.getView_cnt_category2();
                        userViewLogEntity.setView_cnt_category2(currentCnt2 != null ? currentCnt2 + 1 : 1);
                        break;
                }
            }


            userViewLogRepository.save(userViewLogEntity);
        } else {
            throw new DataNotFoundException("게시글이 존재하지 않습니다.");
        }

    }


    // 조회 기록 사용해 분포 계산
    public Map<String, Float> userviewdistribution(String user_seq){
        UserViewLogEntity userViewLogEntity = userViewLogRepository.findByUserSeq(user_seq);
        if (userViewLogEntity != null){
            Map<String, Float> viewCounts = new HashMap<>();
            viewCounts.put("코딩", userViewLogEntity.getView_cnt_category1());
            viewCounts.put("소설", userViewLogEntity.getView_cnt_category2());



            float sumOfViews= 0.0f;

            for (String category : categories) {
                sumOfViews += viewCounts.get(category);
            }

            // 분포 계산
            Map<String, Float> viewdistribution = new HashMap<>();

            for (String category : categories) {
                viewdistribution.put(category, viewCounts.get(category) / sumOfViews);
            }
            System.out.println(viewdistribution);
            return viewdistribution;
        }
        else{
            throw new DataNotFoundException("아직 조회한 글이 없습니다");
        }
    }


    // 좋아요 기록 카운트 후 분포 계산
    public Map<String, Float> userlikeddistribution(String user_seq){

        Map<String, Float> likeddistribution = new HashMap<>();

        // 사용자의 좋아요 목록을 가져옵니다.
        UsersLikesEntity userLikeList = userLikeRepository.findByUserSeq(user_seq)
                .orElseThrow(() -> new DataNotFoundException("Invalid user_seq: " + user_seq));

        // 좋아요한 게시글의 ID 목록을 가져옵니다.
        List<Long> likedBoardIds = userLikeList.getBoardGiveId();

        for (Long id : likedBoardIds) {
            // 각 게시글의 정보를 가져옵니다.
            BoardEntity board = boardRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Invalid board_give_id: " + id));

            // 카테고리 ID를 가져옵니다.
            String categoryId = board.getCategory_id();

            // 카테고리별 개수를 세어줍니다.
            likeddistribution.put(categoryId, likeddistribution.getOrDefault(categoryId, 0.0f) + 1);
        }

        // 분포 계산
        int sum = likeddistribution.values().stream().mapToInt(Float::intValue).sum();

        for (Map.Entry<String, Float> entry : likeddistribution.entrySet()) {
            entry.setValue((entry.getValue() / sum) + 1);
        }

        for (String category : categories) {
            if (!likeddistribution.containsKey(category)) {
                likeddistribution.put(category, 1.0f);
            }
        }

        System.out.println(likeddistribution);
        return likeddistribution;

    }

    public void finaldistribution(String user_seq){
        Map<String, Float> viewDist = userviewdistribution(user_seq);
        Map<String, Float> likeDist = userlikeddistribution(user_seq);

        Map<String, Float> finalDist = new HashMap<>();

        for (String category : categories) {
            float viewValue = viewDist.get(category);
            float likeValue = likeDist.get(category);

            float finalValue = viewValue * likeValue;
            finalDist.put(category, finalValue);
        }

        FinalDistributionEntity finalDistributionEntity = finalDistributionRepository.findByUserSeq(user_seq);

        if (finalDistributionEntity == null){
            UserViewLogEntity user = userViewLogRepository.findById(user_seq).orElseThrow(() -> new DataNotFoundException("해당하는 유저가 없습니다."));
            finalDistributionEntity = new FinalDistributionEntity();
            finalDistributionEntity.setUser(user);
        }


        UsersLikesEntity userLikeList = userLikeRepository.findByUserSeq(user_seq)
                .orElseThrow(() -> new DataNotFoundException("Invalid user_seq: " + user_seq));

        for (int i=0; i<categories.size(); i++) {
            String category = categories.get(i);

            if ("코딩".equals(category)) {
                finalDistributionEntity.setView_cnt_category1(finalDist.get(category));
            } else if ("소설".equals(category)) {
                finalDistributionEntity.setView_cnt_category2(finalDist.get(category));
            }
            // 새로운 카테고리가 추가될 경우 여기에 조건문을 추가하면 됩니다.
            // 예: else if ("과학".equals(category)) { ... }
        }


        finalDistributionRepository.save(finalDistributionEntity);

    }



    // 분포를 이용해 추천 글 불러오기
    public List<BoardDTO> recommendedboardid(String user_seq){
        finaldistribution(user_seq);
        FinalDistributionEntity finalDistributionEntity = finalDistributionRepository.findByUserSeq(user_seq);
        if(finalDistributionEntity != null){

            Map<String, Float> categoryViews = new HashMap<>();
            categoryViews.put("코딩", finalDistributionEntity.getView_cnt_category1());
            categoryViews.put("소설", finalDistributionEntity.getView_cnt_category2());

            List<Map.Entry<String, Float>> sortedEntries = categoryViews.entrySet().stream()
                    .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                    .collect(Collectors.toList());

            String maxCategory = sortedEntries.get(0).getKey();

            List<BoardEntity> boardEntities = boardRepository.findTop3ByCategoryIdRandom(maxCategory);

            return boardEntities.stream()
                    .map(BoardDTO::toBoardDTO)
                    .collect(Collectors.toList());

        }else{
            throw new DataNotFoundException("추천받으실려면 글을 조회하세요");
        }

    }

}

