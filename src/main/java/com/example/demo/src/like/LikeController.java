package com.example.demo.src.like;
import com.example.demo.config.*;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.like.model.*;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/likes")
public class LikeController {

        final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Autowired
        private final LikeProvider likeProvider;
        @Autowired
        private final LikeService likeService;
        @Autowired
        private final JwtService jwtService;

        public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
            this.likeProvider = likeProvider;
            this.likeService = likeService;
            this.jwtService = jwtService;
        }

        /**
         * 상품 찜 등록 API
         * [POST] /likes/:productIdx
         * @return BaseResponse<String>
         */
        // Body
        @ResponseBody
        @PostMapping("/{productIdx}")
        public BaseResponse<String> createLike(@PathVariable("productIdx") int productIdx) {
            try {
                int userIdxByJwt = jwtService.getUserIdx();

                likeService.createLike(userIdxByJwt,productIdx);
                return new BaseResponse<>(SUCCESS);

            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 상품 찜 취소 API
         * [PATCH] /likes/:productIdx
         * @return BaseResponse<String>
         */
        // Body
        @ResponseBody
        @PatchMapping("/{productIdx}")
        public BaseResponse<String> cancelLike(@PathVariable("productIdx") int productIdx) {
            try {
                //jwt에서 idx 추출.
                int userIdxByJwt = jwtService.getUserIdx();

                likeService.cancelLike(userIdxByJwt,productIdx);
                return new BaseResponse<>(SUCCESS);

            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 상품 찜 컬렉션 생성 API
         * [POST] /likes/collections
         * @return BaseResponse<String>
         */
        @ResponseBody
        @PostMapping("/collections")
        public BaseResponse<String> createCollection(@RequestBody PostCollectionReq postCollectionReq) {
            if(postCollectionReq.getName() == null){
                return new BaseResponse<>(POST_COLLECTION_EMPTY_COLLECTION_NAME);
            }
            if(postCollectionReq.getName().length()>10){
                return new BaseResponse<>(POST_COLLECTION_LONG_COLLECTION_NAME);
            }
            try {
                int userIdxByJwt = jwtService.getUserIdx();

                likeService.createCollection(userIdxByJwt,postCollectionReq.getName());
                return new BaseResponse<>(SUCCESS);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 상품 찜 컬렉션 수정 API
         * [PATCH] /likes/collections/:collectionIdx
         * @return BaseResponse<String>
         */

        @ResponseBody
        @PatchMapping("/collections/{collectionIdx}")
        public BaseResponse<String> updateCollection(@PathVariable("collectionIdx") int collectionIdx,@RequestBody PostCollectionReq postCollectionReq) {
            if(postCollectionReq.getName() == null){
                return new BaseResponse<>(POST_COLLECTION_EMPTY_COLLECTION_NAME);
            }
            if(postCollectionReq.getName().length()>10){
                return new BaseResponse<>(POST_COLLECTION_LONG_COLLECTION_NAME);
            }
            try {
                int userIdxByJwt = jwtService.getUserIdx();
                likeService.updateCollection(userIdxByJwt,collectionIdx,postCollectionReq.getName());
                return new BaseResponse<>(SUCCESS);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 찜 컬렉션으로 상품 이동 API
         * [POST] likes/collections/:collectionIdx/:productIdx
         * @return BaseResponse<String>
         */
        @PostMapping("/collections/{collectionIdx}")
        public BaseResponse<String> createCollectionProduct(@PathVariable("collectionIdx") int collectionIdx,@RequestBody PostCollectionProductReq postCollectionProductReq) {
            if(postCollectionProductReq.getProductIdxList() == null){
                return new BaseResponse<>(POST_COLLECTION_PRODUCT_EMPTY_PRODUCTLIST);
            }
            try {
                int userIdxByJwt = jwtService.getUserIdx();
                likeService.createCollectionProduct(userIdxByJwt,collectionIdx,postCollectionProductReq.getProductIdxList());
                return new BaseResponse<>(SUCCESS);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 찜 컬렉션 내부 상품 조회 API
         * [GET] likes/collections/:collectionIdx?status=
         * @return BaseResponse<String>
         */
        @GetMapping("/collections/{collectionIdx}")
        public BaseResponse<List<GetCollectionProductsRes>> getCollectionProducts(@PathVariable("collectionIdx") int collectionIdx,@RequestParam(value = "status") String status) {
            if(!(status.equals("sale") || status.equals("not-sale"))){
                return new BaseResponse<>(GET_COLLECTION_PRODUCTS_INVALID_STATUS);
            }
            try {
                int userIdxByJwt = jwtService.getUserIdx();
                List<GetCollectionProductsRes> result = likeProvider.getCollectionProducts(userIdxByJwt,collectionIdx,status);
                return new BaseResponse<>(result);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }


        /**
         * 상품 찜 컬렉션 삭제 API
         * [DELETE] /likes/collections/:collectionIdx
         * @return BaseResponse<String>
         */

        @ResponseBody
        @DeleteMapping("/collections/{collectionIdx}")
        public BaseResponse<String> deleteCollection(@PathVariable("collectionIdx") int collectionIdx) {
            try {
                int userIdxByJwt = jwtService.getUserIdx();
                likeService.deleteCollection(userIdxByJwt,collectionIdx);
                return new BaseResponse<>(SUCCESS);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
        }

        /**
         * 내 찜 목록 조회/필터링조회 API
         * [GET] /app/likes?order=&status=
         * @return BaseResponse<String>
         */
        @GetMapping("")
        public BaseResponse<GetLikesRes>  getLikes(@RequestParam(value = "order") String order,@RequestParam(value = "status") String status) {
            if(!(order.equals("new") || order.equals("not-past") || order.equals("high") || order.equals("hot") || order.equals("low") || order.equals(""))){
                return new BaseResponse<>(GET_LIKES_INVALID_ORDER);
            }
            if(!(status.equals("sale") || status.equals("not-sale")|| status.equals(""))){
                return new BaseResponse<>(GET_COLLECTION_PRODUCTS_INVALID_STATUS);
            }
            try {
                int userIdxByJwt = jwtService.getUserIdx();
                GetLikesRes result = likeProvider.getLikes(userIdxByJwt,order,status);
                return new BaseResponse<>(result);
            } catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }

        }




}

