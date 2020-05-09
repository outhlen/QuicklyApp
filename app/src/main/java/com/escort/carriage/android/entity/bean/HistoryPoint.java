package com.escort.carriage.android.entity.bean;

import java.util.List;


public class HistoryPoint  {

   private   PointData data;
    private  String sid;
    private  String tid;
    private  String trid;


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public PointData getData() {
        return data;
    }

    public void setData(PointData data) {
        this.data = data;
    }

    public class PointData{
     TimeData timeSlice;
     NodeData nodeDate;
     private String sid;
     private String tid;
     private String trid;

       public TimeData getTimeSlice() {
           return timeSlice;
       }

       public void setTimeSlice(TimeData timeSlice) {
           this.timeSlice = timeSlice;
       }

       public NodeData getNodeDate() {
           return nodeDate;
       }

       public void setNodeDate(NodeData nodeDate) {
           this.nodeDate = nodeDate;
       }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

       public String getTid() {
            return tid;
        }

       public void setTid(String tid) {
            this.tid = tid;
        }

       public String getTrid() {
            return trid;
        }

       public void setTrid(String trid) {
            this.trid = trid;
        }
   }



   public static class TimeData{
       public List<TimeBean> times;

   }

    public static class NodeData{
      public  List<NodeBean> turn;

    }

    public static  class NodeBean{
       private String startDate;
        private String endDate;
        private String tid;
        private String trid;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTrid() {
            return trid;
        }

        public void setTrid(String trid) {
            this.trid = trid;
        }
    }

    public static  class TimeBean{
        private String startDate;
        private String endDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

}
