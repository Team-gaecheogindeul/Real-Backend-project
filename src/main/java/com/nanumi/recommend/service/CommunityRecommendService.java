package com.nanumi.recommend.service;

import com.nanumi.CommunityCategory.entity.CommunityCategoryEntity;
import com.nanumi.CommunityCategory.repository.CommunityCategoryRepository;
import com.nanumi.board_give.DataNotFoundException.DataNotFoundException;
import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.CollegeEntity;
import com.nanumi.community.entity.FreeEntity;
import com.nanumi.community.entity.LearnEntity;
import com.nanumi.community.entity.SchoolEntity;
import com.nanumi.community.entity.UsersLikesEntity.CommunityLikesEntity;
import com.nanumi.community.repository.*;
import com.nanumi.recommend.entity.FinalComDistributionEntity;
import com.nanumi.recommend.entity.UserComViewLogEntity;
import com.nanumi.recommend.repository.FinalComDistributionRepository;
import com.nanumi.recommend.repository.UserComViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityRecommendService {
    private final CollegeRepository collegeRepository;
    private final FreeRepository freeRepository;
    private final LearnRepository learnRepository;
    private final SchoolRepository schoolRepository;
    private final UserComViewLogRepository userComViewLogRepository;
    private final CommunityCategoryRepository communityCategoryRepository;
    private final UsersLikesCommunityRepository userComLikeRepository;
    private final FinalComDistributionRepository finalComDistributionRepository;

    static List<String> categories = Arrays.asList("공부", "일상");
    public void saveviewlog(String user_seq, Long board_id) {

        Optional<CommunityCategoryEntity> ComOptional = communityCategoryRepository.findById(board_id);

        if (ComOptional.isPresent()) {

            CommunityCategoryEntity communityCategory = ComOptional.get();

            UserComViewLogEntity userViewLog = userComViewLogRepository.findByUserSeq(user_seq);

            // Initialize view counts if they are null.
            if (userViewLog == null) {
                userViewLog = new UserComViewLogEntity();
                userViewLog.setUser_seq(user_seq);
                userViewLog.setView_cnt_study(0.0f);
                userViewLog.setView_cnt_daily(0.0f);

            }

            String[] categories = {communityCategory.getNew_category1(), communityCategory.getNew_category2()};

            for (int i = 0; i < 2; i++) {
                switch (categories[i]) {
                    case "공부":
                        float currentCntStudy = userViewLog.getView_cnt_study();
                        userViewLog.setView_cnt_study(currentCntStudy + ((i == 0) ? 1.0f : 0.7f));
                        break;
                    case "일상":
                        float currentCntDaily = userViewLog.getView_cnt_daily();
                        userViewLog.setView_cnt_daily(currentCntDaily + ((i == 0) ? 1.0f : 0.7f));
                        break;
                }
            }
            userComViewLogRepository.save(userViewLog);



        } else {
            throw new DataNotFoundException("카테고리 정보가 존재하지 않습니다.");
        }

    }

    public Map<String, Float> userviewdistribution(String user_seq){
        UserComViewLogEntity userViewLogEntity = userComViewLogRepository.findByUserSeq(user_seq);
        if (userViewLogEntity != null){
            Map<String, Float> viewCounts = new HashMap<>();
            viewCounts.put("공부", userViewLogEntity.getView_cnt_study());
            viewCounts.put("일상", userViewLogEntity.getView_cnt_daily());



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

    public Map<String, Float> userlikeddistribution(String user_seq){

        Map<String, Float> likeddistribution = new HashMap<>();

        // 사용자의 좋아요 목록을 가져옵니다.
        CommunityLikesEntity userLikeList = userComLikeRepository.findByUserSeq(user_seq)
                .orElseThrow(() -> new DataNotFoundException("Invalid user_seq: " + user_seq));

        // 좋아요한 게시글의 ID 목록을 가져옵니다.
        List<Long> likedBoardIds = userLikeList.getBoardId();

        for (Long id : likedBoardIds) {
            // 각 게시글의 정보를 가져옵니다.
            CommunityCategoryEntity category = communityCategoryRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Invalid board_give_id: " + id));

            // 카테고리 ID를 가져옵니다.
            String categoryId = category.getNew_category1();

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

        System.out.println("user_seq:" +user_seq+"likeddistribution"+likeddistribution);
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

        FinalComDistributionEntity finalComDistributionEntity = finalComDistributionRepository.findByUserSeq(user_seq);

        if (finalComDistributionEntity == null){
            UserComViewLogEntity user = userComViewLogRepository.findById(user_seq).orElseThrow(() -> new DataNotFoundException("해당하는 유저가 없습니다."));
            finalComDistributionEntity = new FinalComDistributionEntity();
            finalComDistributionEntity.setUser(user);
        }


        CommunityLikesEntity userLikeList = userComLikeRepository.findByUserSeq(user_seq)
                .orElseThrow(() -> new DataNotFoundException("Invalid user_seq: " + user_seq));

        for (int i=0; i<categories.size(); i++) {
            String category = categories.get(i);

            if ("공부".equals(category)) {
                finalComDistributionEntity.setView_cnt_study(finalDist.get(category));
            } else if ("일상".equals(category)) {
                finalComDistributionEntity.setView_cnt_daily(finalDist.get(category));
            }
            // 새로운 카테고리가 추가될 경우 여기에 조건문을 추가하면 됩니다.
            // 예: else if ("과학".equals(category)) { ... }
        }


        finalComDistributionRepository.save(finalComDistributionEntity);

    }



    public List<CommunityDTO> recommendedboardid(String user_seq, String type) {
        finaldistribution(user_seq);
        FinalComDistributionEntity finalComDistributionEntity = finalComDistributionRepository.findByUserSeq(user_seq);
        if(finalComDistributionEntity != null){

            Map<String, Float> categoryViews = new HashMap<>();
            categoryViews.put("공부", finalComDistributionEntity.getView_cnt_study());
            categoryViews.put("일상", finalComDistributionEntity.getView_cnt_daily());

            List<Map.Entry<String, Float>> sortedEntries = categoryViews.entrySet().stream()
                    .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                    .collect(Collectors.toList());

            String maxCategory = sortedEntries.get(0).getKey();

            switch (type){
                case "Free":
                    List<FreeEntity> FreeEntities = freeRepository.findTop3ByCategoryIdRandom(maxCategory);
                    return FreeEntities.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList());
                case "College":
                    List<CollegeEntity> CollegeEntities = collegeRepository.findTop3ByCategoryIdRandom(maxCategory);
                    return CollegeEntities.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList());
                case "School":
                    List<SchoolEntity> SchoolEntities = schoolRepository.findTop3ByCategoryIdRandom(maxCategory);
                    return SchoolEntities.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList());
                case "Learn":
                    List<LearnEntity> LearnEntities = learnRepository.findTop3ByCategoryIdRandom(maxCategory);
                    return LearnEntities.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList());
                default:
                    throw new IllegalArgumentException("Invalid type: " + type);
            }

        } else {
            throw new DataNotFoundException("추천받으실려면 글을 조회하세요");
        }

    }

}
