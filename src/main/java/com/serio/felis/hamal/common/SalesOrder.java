package com.serio.felis.hamal.common;

public class SalesOrder {
	
	/**
	 * 禁用/删除/不活动/无效 状态
	 */
	public static final String	STATUS_DISABLE		= "0";

	/**
	 * 活动/有效 状态
	 */
	public static final String	STATUS_ENABLE		= "1";

	/**
	 * 默认状态为有效状态
	 */
	public static final String	DEFAULT_STATUS		= STATUS_ENABLE;
	
	/**
	 * 生命周期：禁用/无效
	 */
	public static final String LIFECYCLE_DISABLE   = "0";
	
	
	/**
	 * 生命周期：可用/有效
	 */
	public static final String LIFECYCLE_ENABLE    = "1";
	
	/**
	 * 生命周期：逻辑删除
	 */
	public static final String LIFECYCLE_DELETED   = "2";
	
	/**
	 * 生命周期：未激活
	 */
	public static final String LIFECYCLE_UNACTIVE  = "3";
	/**
	 * 生命周期：初始化 (对于有些要求用户在添加后，再次进行设置的实体，可以有初始化状态)
	 */
	public static final String LIFECYCLE_INIT      = "5";

    /** 1新建. */
    public static final String SALES_ORDER_STATUS_NEW = "1";

    /** 3 已同步oms. */
    public static final String SALES_ORDER_STATUS_TOOMS = "3";

    /** 4 库存已确认. */
    public static final String SALES_ORDER_STATUS_CONFIRMED = "4";

    /** 5 库房准备中. */
    public static final String SALES_ORDER_STATUS_WH_HANDLING = "5";

    /** 6 在途. */
    public static final String SALES_ORDER_STATUS_DELIVERIED = "6";

    /** 9 会员取消. */
    public static final String SALES_ORDER_STATUS_CANCELED = "9";

    /** 10 商城取消. */
    public static final String SALES_ORDER_STATUS_SYS_CANCELED = "10";

    /** 15 交易完成. */
    public static final String SALES_ORDER_STATUS_FINISHED = "15";

    //---------------------------------------------------------------------------------------

    // so finance status
    /** 财务状态:未收款 1. */
    public static final String SALES_ORDER_FISTATUS_NO_PAYMENT = "1";

    /** 财务状态:货到付款 2. */
    public static final String SALES_ORDER_FISTATUS_COD = "2";

    /** 财务状态:已全额收款 3. */
    public static final String SALES_ORDER_FISTATUS_FULL_PAYMENT = "3";

    /** 财务状态:已部分收款4. */
    public static final String SALES_ORDER_FISTATUS_PART_PAYMENT = "4";

    //---------------------------------------------------------------------------------------

    /** 1 货到付款. */
    public static final String SO_PAYMENT_TYPE_COD = "1";

    /** 2 银行电汇. */
    public static final String SO_PAYMENT_TYPE_TELETRANSFER = "2";

    /** 3 网银在线. */
    public static final String SO_PAYMENT_TYPE_NETPAY = "3";

    /** 4 微信支付. */
    public static final String SO_PAYMENT_TYPE_WECHAT = "4";

    /** 6 支付宝. */
    public static final String SO_PAYMENT_TYPE_ALIPAY = "6";

    /**
     * 7 快钱.
     * 
     * @deprecated 根本不用
     */
    @Deprecated
    public static final String SO_PAYMENT_TYPE_99BILL = "7";

    /** 10 预付卡 只有全额抵扣才会设置此种支付类型. */
    public static final String SO_PAYMENT_TYPE_PREPAID_CARD = "10";

    /** 11 财付通. */
    public static final String SO_PAYMENT_TYPE_TENPAY = "300";

    /** 12 外部积分兑换. */
    public static final String SO_PAYMENT_TYPE_EXTERNAL_POString = "12";

    /** 新华一成卡. */
    public static final String SO_PAYMENT_TYPE_XINHUA_CARD = "104";

    /** LEVIS淘宝B2C－支付宝. */
    public static final String SO_PAYMENT_TYPE_LEVIS_ALIPAY_B2C = "108";

    /** 百付宝（汇付天下）. */
    public static final String SO_PAYMENT_TYPE_BAIFUBAO = "200";

    /** 现金收款. */
    public static final String SO_PAYMENT_TYPE_SASH = "9";

    /** 14 信用卡-支付宝. */
    public static final String SO_PAYMENT_TYPE_ALIPAY_CREDIT = "14";

    /** 18支付宝-快捷支付网关接口. */
    public static final String SO_PAYMENT_TYPE_ALIPAY_EXPRESS = "18";

    /** 支付宝信用卡分期付款. */
    public static final String SO_PAYMENT_TYPE_ALIPAY_GREDITCARDINSTALLMENT = "19";

    /** 零元购. */
    public static final String SO_PAYMENT_TYPE_ZERO_PURCHASE = "20";

    /** 320 银联支付. */
    public static final String SO_PAYMENT_TYPE_UNIONPAY = "320";

    /** 21 分期支付. */
    public static final String SO_PAYMENT_TYPE_PERIODS = "21";

    /** 22 信用卡支付. */
    public static final String SO_PAYMENT_TYPE_CREDIT_CARD = "22";

    /** 710 Paypal */
    public static final String SO_PAYMENT_TYPE_PAYPAL = "710";

    /** 720 PayDoolar */
    public static final String SO_PAYMENT_TYPE_PAYDOOLAR = "720";

    /** 131 支付宝-国际卡 */
    public static final String SO_PAYMENT_TYPE_StringERNATIONALCARD = "131";

    // -----------------------------------------------------------------

    /** 商城正常下单. */
    public static final String SO_SOURCE_NORMAL = "1";

    /** Shopdog正常下单. */
    public static final String SO_SOURCE_SHOPDOG_NORMAL = "2";

    /** 手机端正常下单. */
    public static final String SO_SOURCE_MOBILE_NORMAL = "3";

    // -----------------------------------------------------------------

    /** 订单类型 1-普通订单. */
    public static final String NORMAL_ORDER = "1";

    /** 订单类型 2-预售订单. */
    public static final String PRESALE_ORDER = "2";

    /** 支付类型 1-全额付款. */
    public static final String Full_Payment = "1";

    /** 支付类型 2-分阶段付款. */
    public static final String Phased_Payment = "2";

    // -----------------------------------------------------------------

    /** COD收款类型：现金. */
    public static final String COD_TYPE_CASH = "1";

    /** COD收款类型：刷卡. */
    public static final String COD_TYPE_CARD = "2";

}
