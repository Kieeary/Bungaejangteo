package com.example.demo.src.payment;

import com.example.demo.src.payment.model.GetPaymentUserInfoRes;
import com.example.demo.src.payment.model.GetProductInfoRes;
import com.example.demo.src.payment.model.PostOrderInfoReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

@Repository
public class PaymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetProductInfoRes getProductInfo(int productId, int userId) {
        String getProductInfoQuery = "select ProductImages.imageUrl, Products.price, Products.name, Users.bungaePoint, UserPayInfo.payMethod, UserPayInfo.isAgree\n" +
                "                from Users, UserPayInfo, Products inner join (SELECT * FROM  (SELECT * FROM ProductImages ORDER BY createdAt) a GROUP BY productId) ProductImages\n" +
                "                on Products.productId = ProductImages.productId and Products.productId = ?\n" +
                "where Users.id = ?";

        Object[] getProductInfoParams = new Object[]{productId, userId};

        return this.jdbcTemplate.queryForObject(getProductInfoQuery,
                (rs, rowNum) -> new GetProductInfoRes(
                        rs.getString("imageUrl"),
                        rs.getInt("price"),
                        rs.getString("name"),
                        rs.getInt("bungaePoint"),
                        rs.getString("payMethod"),
                        rs.getString("isAgree")
                ), getProductInfoParams);
    }

//    public GetPaymentUserInfoRes getPaymentUserInfo(int productId, int userId) {
//        String GetPaymentUserInfoQuery = "select Users.bungaePoint, UserPayInfo.payMethod, UserPayInfo.isAgree from Users inner join UserPayInfo on UserPayInfo.id = Users.id where Users.id = ?";
//
//        return this.jdbcTemplate.queryForObject(GetPaymentUserInfoQuery,
//                (rs, rowNum) -> new GetPaymentUserInfoRes(
//                        rs.getInt("bungaePoint"),
//                        rs.getString("payMethod"),
//                        rs.getString("isAgree")
//                ), userId);
//
//    }

    public String getProductPrice(int productId) {
        String getProductPriceQuery = "select concat(format(Products.price, 0), '원') as sum\n" +
                "from Products\n" +
                "where Products.productId = ?";
        return this.jdbcTemplate.queryForObject(getProductPriceQuery, String.class, productId);

    }

    public String getProductTax(int productId) {
        String getProductTaxQuery = "select concat('+' ,format(Products.price * 0.035, 0) , '원') as tax\n" +
                "from Products\n" +
                "where Products.productId = ?";

        return this.jdbcTemplate.queryForObject(getProductTaxQuery, String.class, productId);
    }

    public String getSum(int productId) {
        String getSumQuery = "select concat(format((Products.price * 0.035 + Products.price), 0) , '원') as sum\n" +
                "from Products\n" +
                "where Products.productId = ?";

        return this.jdbcTemplate.queryForObject(getSumQuery, String.class, productId);
    }

    public String getUsedPoint(int userId) {
        String getUsedPointQuery = "select concat(format(Users.bungaePoint, 0) , '원') as usedPoint\n" +
                "from Users\n" +
                "where Users.id = ?";

        return this.jdbcTemplate.queryForObject(getUsedPointQuery, String.class, userId);
    }

    public String getFinalSum(int productId, int userId) {
        String getFinalSumQuery = "select concat(format((Products.price * 0.035 + Products.price - Users.bungaePoint) , 0), '원' ) as finalSum\n" +
                "from Users, Products\n" +
                "where Users.id = ? and Products.productId = ?";

        Object[] getFinalSumParams = new Object[]{userId, productId};

        return this.jdbcTemplate.queryForObject(getFinalSumQuery, String.class, getFinalSumParams);

    }

    public int storeOrderInfo(int userId, int productId, PostOrderInfoReq postOrderInfoReq) {
        String storeOrderInfoQuery = "insert into OrderInfo(id, productId, dealCategory, productName, finalPrice, bungaePay, payMethod, isAgree)\n" +
                "values(?,?,?,?,?,?,?,?)";

        Object[] storeOrderInfoParams = new Object[]{userId, productId, postOrderInfoReq.getDealCategory(), postOrderInfoReq.getProductName(),
                postOrderInfoReq.getFinalPrice(), postOrderInfoReq.getBungaePay(), postOrderInfoReq.getPayMethod(), postOrderInfoReq.getIsAgree()};

        return this.jdbcTemplate.update(storeOrderInfoQuery, storeOrderInfoParams);
    }

    public int changeProductState(int productId) {
        String changeProductStateQuery = "update Products set Products.status = 'SOLDOUT' where Products.productId = ?\n";
        return this.jdbcTemplate.update(changeProductStateQuery, productId);
    }

    public int storeBuySellInfo(int userId, int productId) {

        String storeSellInfoQuery = "insert into Sell(id) select Products.userId from Products where Products.productId = ?";
        this.jdbcTemplate.update(storeSellInfoQuery, productId);

        String lastInsertIdStr = "select last_insert_id()";
        String result = this.jdbcTemplate.queryForObject(lastInsertIdStr, String.class);
        int lastInsertId = Integer.parseInt(result);

        String storeBuyInfoQuery = "insert into Buy(id, connectId) value (?, ?)";
        Object[] storeBuyInfoParams = new Object[] {userId, lastInsertId};
        return this.jdbcTemplate.update(storeBuyInfoQuery, storeBuyInfoParams);

    }
}
