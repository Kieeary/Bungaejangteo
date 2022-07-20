package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    EMPTY_PHONENUM(false,2018,"휴대폰 번호를 입력해주세요."),
    INCORRECT_TYPEOF_PHONENUM(false,2019,"휴대폰 번호는 숫자로만 구성되어야 합니다."),
    INCORRECT_SHAPEOF_PHONENUM(false,2020,"정확한 휴대폰 번호를 입력해주세요"),
    POST_USERS_EMPTY_NAME(false,2021,"이름을 입력해주세요"),
    POST_USERS_EMPTY_STORENAME(false,2022,"상점 이름을 입력해주세요"),
    POST_USERS_LONG_STORENAME(false,2023,"상점 이름은 10자 이내로 설정해주세요"),
    INCORRECT_TYPEOF_STORENAME(false,2024,"상점 이름은 한국어,영어,숫자만 사용가능합니다"),

    // [PATCH] /users
    PATCH_USERS_EMPTY_STORENAME(false,2050,"상점 이름을 입력해주세요."),
    PATCH_USERS_LONG_STORENAME(false, 2051, "상점 이름은 10자 이내로 수정해주세요."),
    PATCH_USERS_EMPTY_SHOPURL(false, 2052, "등록할 url을 입력해주세요."),
    PATCH_USERS_LONG_SHOPURL(false,2053, "url은 50자 이내로 입력해주세요"),
    PATCH_USERS_EMPTY_CONTACTTIME(false, 2054, "연락 가능 시간을 입력하세요."),
    PATCH_USERS_EMPTY_DESCRIPTION(false, 2055, "상점 소개를 입력하세요."),
    PATCH_USERS_LONG_DESCRIPTION(false, 2056, "상점 소개를 1000자 이내로 입력하세요."),
    PATCH_USERS_EMPTY_POLICY(false, 2057, "교환/반품/환불 정책을 입력하세요."),
    PATCH_USERS_LONG_POLICY(false, 2058, "교환/반품/환불 정책을 1000자 이내로 입력하세요."),
    PATCH_USERS_EMPTY_PRECAUTIONS(false, 2059, "구매전 유의사항을 입력하세요."),
    PATCH_USERS_LONG_PRECAUTIONS(false, 2060, "구매전 유의사항은 1000자 이내로 입력하세요."),

    //[POST] /products
    POST_REPORT_EMPTY_PRODUCT_IDX(false,2071,"상품 idx값을 입력해주세요"),
    POST_REPORT_EMPTY_REPORT_TYPE(false,2072,"신고 유형 코드를 입력해주세요"),
    INVALIDT_REPORT_TYPE(false,2073,"유효한 신고 유형 코드가 아닙니다"),

    POST_PRODUCT_EMPTY_PRODUCT_NAME(false,2075,"상품 이름을 입력해주세요"),
    POST_PRODUCT_EMPTY_CATEGORY(false,2076,"상품 카테고리 코드를 입력해주세요"),
    POST_PRODUCT_PRODUCT_EMPTY_PRICE(false,2077,"상품 가격을 입력해주세요"),
    POST_PRODUCT_EMPTY_DESCRIPTION(false,2078,"상품 설명을 입력해주세요"),
    POST_PRODUCT_PRODUCT_EMPTY_IMAGES(false,2079,"상품 이미지를 최소 1개 넣어주세요"),
    INCORRECT_TYPEOF_PRODUCT_INT(false,2080,"상품인덱스, 가격, 카테고리코드는 숫자만 입력해주세요"),
    INCORRECT_SHAPEOF_LATITUDE(false,2081,"올바른 위도 형식이 아닙니다"),
    INCORRECT_SHAPEOF_LONGITUDE(false,2082,"올바른 경도 형식이 아닙니다"),
    INVALIDT_CATEGORY_CODE_TYPE(false,2083,"유효한 카테고리 코드가 아닙니다"),

    //[POST] likes/collections
    POST_COLLECTION_EMPTY_COLLECTION_NAME(false,2090,"찜 컬렉션명을 입력해주세요"),
    POST_COLLECTION_LONG_COLLECTION_NAME(false, 2091, "찜 컬렉션명을 10자 이내로 입력해주세요."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    POST_USERS_EXISTS_STORENAME(false, 3015, "이미 존재하는 상점 이름입니다"),

    // sms
    FAILED_TO_COOLSMS(false,3015,"CoolSMS Error."),

    FAILED_TO_CHECK_AUTH(false,3016,"인증 번호가 일치하지 않습니다."),

    NOT_AVALIABLE_USER_STORE(false,3020, "조회할 수 없는 유저의 상점입니다."),

    //[POST] products/report
    POST_REPORT_EXIST_REPORT(false, 3021, "이미 신고한 상품입니다"),

    //[POST] favorites
    NOT_AVALIABLE_ADD_FOLLOW(false, 3040, "해당 상점은 팔로잉 할 수 없는 상태입니다."),

    //[DELETE] favorites
    FOLLOW_CANCEL_FAIL(false, 3050, "팔로위 취소 하지 못했습니다."),

    //[POST] likes
    FAILED_TO_PRODUCT_LIKE(false,3051,"찜 생성에 실패하였습니다"),
    //[PATCH] likes
    FAILED_TO_CANCEL_LIKE(false,3052,"찜 취소에 실패하였습니다"),
    //[POST] likes/collections
    FAILED_TO_CREATE_COLLECTION(false,3053,"찜 컬렉션 생성에 실패하였습니다"),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_STORENAME(false,4014,"상점이름 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
