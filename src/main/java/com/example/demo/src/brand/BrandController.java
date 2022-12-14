package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.brand.*;
import com.example.demo.src.brand.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.GET_COLLECTION_PRODUCTS_INVALID_STATUS;

@RestController
@RequestMapping("/app/brands")
public class BrandController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BrandProvider brandProvider;
    @Autowired
    private final BrandService brandService;
    @Autowired
    private final JwtService jwtService;

    public BrandController(BrandProvider brandProvider, BrandService brandService, JwtService jwtService) {
        this.brandProvider = brandProvider;
        this.brandService = brandService;
        this.jwtService = jwtService;
    }

    /**
     * 전체메뉴- 브랜드 리스트 조회/정렬API
     * [GET] /app/brands?order=&follow=
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetBrandListRes>> getBrandList(@RequestParam(value = "order") String order,@RequestParam(value = "follow") String follow) {
        if(!(order.equals("korean") || order.equals("english") || order.equals(""))){
            return new BaseResponse<>(GET_BRANDS_INVALID_ORDER);
        }
        if(!(follow.equals("false") || follow.equals("true") || follow.equals(""))){
            return new BaseResponse<>(GET_BRANDS_INVALID_FOLLOW);
        }
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetBrandListRes> result = brandProvider.getBrandList(userIdxByJwt,order,follow);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *전체메뉴- 브랜드 검색 API
     * [GET] /app/brands/search?searchword=
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("/search")
    public BaseResponse<List<GetBrandListRes>> getSearchBrandList(@RequestParam(value = "searchword") String searchWord) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetBrandListRes> result = brandProvider.getSearchBrandList(userIdxByJwt,searchWord);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *홈- 팔로우한 브랜드 조회
     * [GET] /app/brands/follow
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("/follow")
    public BaseResponse<List<getFollowBrandRes>> getFollowedBrandList() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<getFollowBrandRes> result = brandProvider.getFollowedBrandList(userIdxByJwt);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *홈- 추천 브랜드 조회
     * [GET] /app/brands/recommend
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("/recommend")
    public BaseResponse<List<getFollowBrandRes>> getRecommendBrandList() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<getFollowBrandRes> result = brandProvider.getRecommendBrandList(userIdxByJwt);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *검색- 추천 브랜드 조회
     * [GET] /app/brands/searches/recommend
     * @return BaseResponse<List<GetBrandListRes>>
     */
    @GetMapping("/searches/recommend")
    public BaseResponse<List<GetBrandListRes>> getSearchRecommendBrandList() {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            List<GetBrandListRes> result = brandProvider.getSearchRecommendBrandList(userIdxByJwt);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
