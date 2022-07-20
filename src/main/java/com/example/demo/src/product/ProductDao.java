package com.example.demo.src.product;

//import com.example.demo.src.product.model*
import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductRes;
import com.example.demo.src.product.model.PostProductReq;
import com.example.demo.src.product.model.PostReportReq;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.sql.DataSource;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductRes> getRecommendProducts(int userIdx) {
        String GetProductQuery = "SELECT Products.productId,name,price,region,isSafePayment," +
                "case" +
                "    when (TIMESTAMPDIFF(WEEK,createdAt,NOW()) >= 1)  then CONCAT(TIMESTAMPDIFF(WEEK,createdAt,NOW()), '주전')" +
                "    when (TIMESTAMPDIFF(DAY,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(WEEK,createdAt,NOW()) < 1)  then CONCAT(TIMESTAMPDIFF(DAY,createdAt,NOW()), '일전')" +
                "    when (TIMESTAMPDIFF(HOUR,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(HOUR,createdAt,NOW()), '시간전')" +
                "    when (TIMESTAMPDIFF(MINUTE,createdAt,NOW()) >= 1 AND TIMESTAMPDIFF(DAY,createdAt,NOW()) < 1) then CONCAT(TIMESTAMPDIFF(MINUTE,createdAt,NOW()), '분전')" +
                " END AS elapsedTime, imageUrl,likeCount" +
                " FROM (Products left outer join (SELECT imageUrl, productId" +
                " FROM (SELECT *" +
                "       FROM ProductImages" +
                "       ORDER BY createdAt)b" +
                " GROUP BY productId" +
                " )as imageTable" +
                " on imageTable.productId = Products.productId)" +
                " left outer join (select productId,count(*) as likeCount from Likes group by productId)as c" +
                " on Products.productId =  c.productId order by rand() limit 10" ;

        return this.jdbcTemplate.query(GetProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("Products.productId"),
                        rs.getString("imageUrl"),
                        this.jdbcTemplate.queryForObject("select exists(select * from Likes where userId = ? AND productId = ?) as b",
                              (rs2, rowNum2) -> new Integer(
                                      rs2.getInt("b")),
                                userIdx,rs.getInt("Products.productId")),
                        rs.getInt("price"),
                        rs.getString("name"),
                        rs.getString("region"),
                        rs.getString("elapsedTime"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("likeCount")
                       ));
    }

    public int  createReport(int userIdx, Integer productIdx,PostReportReq postReportReq) throws BaseException {
        String createReportQuery = "insert into ProductReports (userId, productId, reportType, detailReason) VALUES (?,?,?,?)";
        Object[] createReportParams = new Object[]{userIdx,productIdx, postReportReq.getReportType(),postReportReq.getDetailReason()};
        return this.jdbcTemplate.update(createReportQuery, createReportParams);
    }

    public int getReport(int userIdx,int productIdx) throws BaseException{
        String getReportQuery = "select exists(select * from ProductReports where userId = ? and productId = ?)";
        Object[] getReportParams = new Object[]{userIdx,productIdx};
        return this.jdbcTemplate.queryForObject(getReportQuery,
                int.class,
                getReportParams);

    }

    public int createProduct(int userIdx, PostProductReq postProductReq) throws BaseException {
        String createProductQuery = "insert into Products(userId,name,category,price,isDeliveryIncluded,count,isOld,isExchangeAvailable,isSafePayment,region,latitude,longitude,description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{userIdx,postProductReq.getName(),postProductReq.getCategory(),postProductReq.getPrice(),postProductReq.getIsDeliveryIncluded(),postProductReq.getCount(),
                postProductReq.getIsOld(),postProductReq.getIsExchangeAvailable(),postProductReq.getIsSafePayment(),postProductReq.getRegion(),postProductReq.getLatitude(),postProductReq.getLongitude(),postProductReq.getDescription()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }


    @Transactional
   public int createProductImages(int productIdx, List<String> Images) throws BaseException {
        int result = 0;
        for (int i = 0 ; i < Images.size(); i++){
            String createProductImagesQuery = "insert into ProductImages (productId,imageUrl) VALUES (?,?)";
            Object[] createProductImagesParams = new Object[]{productIdx,(Images.get(i))};
            result += this.jdbcTemplate.update(createProductImagesQuery, createProductImagesParams);
        }
        return result;
    }

    @Transactional
    public int createProductTags(int productIdx, List<String> tags) throws BaseException {
        int result = 0;
        for (int i = 0 ; i < tags.size(); i++){
            String createProductTagsQuery = "insert into ProductTags (productId,tag) VALUES (?,?)";
            Object[] createProductTagsParams = new Object[]{productIdx,(tags.get(i))};
            result += this.jdbcTemplate.update(createProductTagsQuery, createProductTagsParams);
        }
        return result;
    }


}

