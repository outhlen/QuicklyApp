package com.escort.carriage.android.entity.response.home;

import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

public class NewsDetailsBean extends ResponceJsonEntity<NewsDetailsBean.DataBean> {

    private Object orgMessage;



    public Object getOrgMessage() {
        return orgMessage;
    }

    public void setOrgMessage(Object orgMessage) {
        this.orgMessage = orgMessage;
    }

    public static class DataBean {
        /**
         * id : 1236214820001935362
         * title : 公共服务在2019：增长、创新
         * subTitle : 公共服务物流
         * author : admin
         * source : 原创
         * keywords : 物流
         * intro : 公共服务物流
         * newsType : 1
         * content : <p><span style="font-family: 宋体; text-indent: 24px;">&nbsp;&nbsp;&nbsp;&nbsp;刚刚过去的2019年，对于物流业来说，是非常值得一提的一年。在这一年，物流业跑赢了中国大多数实体经济部门的增速。更为重要的是，高增长的背后，不仅是需求的刺激，更有市场结构优化和技术变革等供给端的改变。</span></p><p><span style="font-family: 宋体; text-indent: 24px;">&nbsp; &nbsp;<img src="http://huipushenghuo.oss-cn-beijing.aliyuncs.com/20200307/64a3f72d839b44248d59dbf948c0e012.png" title="" alt="image.png"/></span></p><p><span style="font-family: 宋体; text-indent: 24px;"><span style="font-family: 宋体; text-indent: 24px; font-size: 12pt;">&nbsp;&nbsp;&nbsp;&nbsp;不过，在经历创新和增长的同时，我们发现物流从业人员普遍怀有较大的危机感。这是因为市场竞争的白热化、疲软需求的潜在预期触动着他们的神经。而2019年，这样的趋势很可能持续下去。&nbsp;</span><br/><br/><span style="font-family: 宋体; text-indent: 24px; font-size: 12pt; font-weight: bold;">增长，仍将保持较快速度&nbsp;</span><br/><br/><span style="font-family: 宋体; text-indent: 24px; font-size: 12pt;">近几年，经济环境不景气，物流是少数几个保持高于平均水准增长的行业之一。截至11月，2019年全国社会物流总费用接近12万亿元，同比增长13.3%（参见图1），显著高于GDP增速，显示了经济对物流的需求仍然强劲。</span>&nbsp;</span></p>
         * imageUrl1 : http://huipushenghuo.oss-cn-beijing.aliyuncs.com/20200307/64a3f72d839b44248d59dbf948c0e012.png
         * imageUrl2 :
         * imageUrl3 :
         * imageUrl4 :
         * imageUrl5 :
         * imageUrl6 :
         * imageUrl7 :
         * imageUrl8 :
         * imageUrl9 :
         * publishTime : null
         * operater : ceshi
         * operateTime : 1583542755000
         * companyId : null
         */

        private String id;
        private String title;
        private String subTitle;
        private String author;
        private String source;
        private String keywords;
        private String intro;
        private String newsType;
        private String content;
        private String imageUrl1;
        private String imageUrl2;
        private String imageUrl3;
        private String imageUrl4;
        private String imageUrl5;
        private String imageUrl6;
        private String imageUrl7;
        private String imageUrl8;
        private String imageUrl9;
        private Object publishTime;
        private String operater;
        private long operateTime;
        private Object companyId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getNewsType() {
            return newsType;
        }

        public void setNewsType(String newsType) {
            this.newsType = newsType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImageUrl1() {
            return imageUrl1;
        }

        public void setImageUrl1(String imageUrl1) {
            this.imageUrl1 = imageUrl1;
        }

        public String getImageUrl2() {
            return imageUrl2;
        }

        public void setImageUrl2(String imageUrl2) {
            this.imageUrl2 = imageUrl2;
        }

        public String getImageUrl3() {
            return imageUrl3;
        }

        public void setImageUrl3(String imageUrl3) {
            this.imageUrl3 = imageUrl3;
        }

        public String getImageUrl4() {
            return imageUrl4;
        }

        public void setImageUrl4(String imageUrl4) {
            this.imageUrl4 = imageUrl4;
        }

        public String getImageUrl5() {
            return imageUrl5;
        }

        public void setImageUrl5(String imageUrl5) {
            this.imageUrl5 = imageUrl5;
        }

        public String getImageUrl6() {
            return imageUrl6;
        }

        public void setImageUrl6(String imageUrl6) {
            this.imageUrl6 = imageUrl6;
        }

        public String getImageUrl7() {
            return imageUrl7;
        }

        public void setImageUrl7(String imageUrl7) {
            this.imageUrl7 = imageUrl7;
        }

        public String getImageUrl8() {
            return imageUrl8;
        }

        public void setImageUrl8(String imageUrl8) {
            this.imageUrl8 = imageUrl8;
        }

        public String getImageUrl9() {
            return imageUrl9;
        }

        public void setImageUrl9(String imageUrl9) {
            this.imageUrl9 = imageUrl9;
        }

        public Object getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(Object publishTime) {
            this.publishTime = publishTime;
        }

        public String getOperater() {
            return operater;
        }

        public void setOperater(String operater) {
            this.operater = operater;
        }

        public long getOperateTime() {
            return operateTime;
        }

        public void setOperateTime(long operateTime) {
            this.operateTime = operateTime;
        }

        public Object getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Object companyId) {
            this.companyId = companyId;
        }
    }
}
