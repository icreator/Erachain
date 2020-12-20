package org.erachain.core.exdata;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ExPaysTest {

    private int flags;

    private Long assetKey = 2L;
    private int balancePos = 1;
    private boolean backward = false;
    private BigDecimal amountMin = BigDecimal.ONE;
    private BigDecimal amountMax = BigDecimal.TEN;

    private int payMethod = 0; // 0 - by Total, 1 - by Percent
    private BigDecimal payMethodValue = BigDecimal.TEN;

    private Long filterAssetKey = 10L;
    private int filterBalancePos = 3;
    private int filterBalanceSide = 2;
    private BigDecimal filterBalanceLessThen = BigDecimal.ONE;
    ;
    private BigDecimal filterBalanceMoreThen = BigDecimal.TEN;

    private Integer filterTXType = 31;
    private Long filterTXStartSeqNo = 124213435L;
    private Long filterTXEndSeqNo = 3253245325L;

    private final Integer filterByGender = 1; // = gender or all
    private boolean selfPay = false;

    @Test
    public void toByte() {

        ExPays exPays = new ExPays(flags, assetKey, balancePos, backward, amountMin, amountMax,
                payMethod, payMethodValue, filterAssetKey, filterBalancePos, filterBalanceSide,
                filterBalanceLessThen, filterBalanceMoreThen,
                filterTXType, filterTXStartSeqNo, filterTXEndSeqNo,
                filterByGender, selfPay);
        byte[] bytes = null;
        try {
            bytes = exPays.toByte();
        } catch (Exception e) {
        }
        assertEquals(bytes.length, exPays.length());

    }

    @Test
    public void length() {
    }

    @Test
    public void parse() {
    }

    @Test
    public void make() {
    }
}