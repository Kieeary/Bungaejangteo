package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;


@RestController
@RequestMapping("/app/reviews")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }


    /**
     * 리뷰 조회
     *
     * @param id
     * @return BaseResponse<GetReviewRes>
     */
    @GetMapping("/{id}")
    @ResponseBody
    public BaseResponse<List<GetReviewRes>> getReviewResBaseResponse(@PathVariable("id") int id) throws BaseException {

        Timestamp dateNow = new Timestamp(System.currentTimeMillis());

        List<GetReviewRes> getReviewRes = reviewProvider.getReviewRes(id);
        return new BaseResponse<>(getReviewRes);
    }
}
