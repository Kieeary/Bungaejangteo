package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.src.brand.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BrandProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandDao brandeDao;
    private final JwtService jwtService;

    @Autowired
    public BrandProvider(BrandDao brandeDao, JwtService jwtService) {
        this.brandeDao = brandeDao;
        this.jwtService = jwtService;
    }

    public List<GetBrandListRes> getBrandList(int userIdx,String order,String follow) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {

            List<GetBrandListRes> getBrandListRes = brandeDao.getBrandList(userIdx,order,follow);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrandListRes> getSearchBrandList(int userIdx,String searchWord) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<GetBrandListRes> getBrandListRes = brandeDao.getSearchBrandList(userIdx,searchWord);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<getFollowBrandRes> getFollowedBrandList(int userIdx) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<getFollowBrandRes> getBrandListRes = brandeDao.getFollowedBrandList(userIdx);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<getFollowBrandRes> getRecommendBrandList(int userIdx) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<getFollowBrandRes> getBrandListRes = brandeDao.getRecommendBrandList(userIdx);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrandListRes> getSearchRecommendBrandList(int userIdx) throws BaseException {
        if (isDeletedUser(userIdx) == 1){
            throw new BaseException(DELETED_USER);
        }
        try {
            List<GetBrandListRes> getBrandListRes = brandeDao.getSearchRecommendBrandList(userIdx);
            return getBrandListRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int isDeletedUser(int userIdx) throws BaseException {
        try {
            int result = brandeDao.isDeletedUser(userIdx);
            System.out.println(result);
            return brandeDao.isDeletedUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}