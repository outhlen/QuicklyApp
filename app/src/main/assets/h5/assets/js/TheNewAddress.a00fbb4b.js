(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["TheNewAddress"],{2395:function(t,e,s){"use strict";var n=s("2969"),a=s.n(n);a.a},"265d":function(t,e,s){"use strict";var n=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"BaseBottomBtn"},[s("div",{staticClass:"bottom_btn",on:{click:t.e_bottombtn}},[t._v("\n        "+t._s(t.btnLabel)+"\n    ")])])},a=[],i={data:function(){return{}},components:{},methods:{e_bottombtn:function(){this.$emit("bottomClick",!0)}},props:{btnLabel:{type:String,default:"确定"}},mounted:function(){},created:function(){}},r=i,o=(s("afd0"),s("2877")),c=Object(o["a"])(r,n,a,!1,null,"4ee0e8fa",null);e["a"]=c.exports},2969:function(t,e,s){},"577d":function(t,e,s){},"59e6":function(t,e,s){},"97cb":function(t,e,s){"use strict";s.r(e);var n=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"TheNewAddress"},[s("div",{staticClass:"address_tab"},t._l(t.tabList,(function(e,n){return s("div",{key:n,staticClass:"tabList",class:t.currentTabId==e.id?"acticeTab":"",on:{click:function(s){return s.stopPropagation(),t.e_selectTab(e.id)}}},[s("span",{staticClass:"tablabel"},[t._v(t._s(e.label))])])})),0),t._l(t.addressList,(function(e,n){return s("div",{key:n,staticClass:"TNA_background",on:{click:function(s){return s.stopPropagation(),t.e_selectAddress(e)}}},[s("span",{staticClass:"TNA_b_left"},[t._v(t._s(e.addressee))]),s("span",{staticClass:"TNA_b_right"},[t._v(t._s(e.phone))]),1==e.isdefault?s("button",{staticClass:"TNA_b_button"},[t._v("默认地址")]):t._e(),s("img",{staticClass:"editIcon",attrs:{src:"https://dongfangbaode.oss-cn-qingdao.aliyuncs.com/xiaoer_xiaochengxu/img/9-1%E5%9C%B0%E5%9D%80%E8%96%84/%E7%BC%96%E8%BE%91%20%E6%8B%B7%E8%B4%9D%202.png"},on:{click:function(s){return s.stopPropagation(),t.e_editAddress(e)}}}),s("div",{staticClass:"TNA_b_content"},[s("span",[t._v(t._s(e.address)+t._s(e.detailed))]),s("img",{staticClass:"deleteIcon",attrs:{src:"https://xiaoeryabiao.oss-cn-beijing.aliyuncs.com/laji.png"},on:{click:function(s){return s.stopPropagation(),t.e_deleteAddress(e.id,n)}}})])])})),s("div",{staticClass:"TNA_button",on:{click:function(e){return e.stopPropagation(),t.e_goAddAddress(e)}}},[s("BaseBottomBtn",{attrs:{btnLabel:"新建地址信息"},on:{bottomClick:t.bottomClick}})],1),t.mymessage?s("BaseMessage",{attrs:{message:t.mymessage}}):t._e(),s("van-dialog",{attrs:{id:"van-dialog"}})],2)},a=[],i=(s("96cf"),s("3b8d")),r=s("265d"),o=s("2241"),c={data:function(){return{Dialog:o["a"],addressList:[],mymessage:"",pageNumber:0,pageSize:10,isLastPage:!1,tabList:[{id:0,label:"发货地址"},{id:1,label:"收货地址"}],currentTabId:0}},components:{BaseBottomBtn:r["a"]},methods:{e_selectTab:function(t){this.currentTabId=t,this.addressList=[],this.pageNumber=0,this.getAddressList()},myMessage:function(t){var e=this;this.$my_message=t,setTimeout((function(){e.$my_message=""}),1500)},clickChange1:function(t){console.log(t)},getAddressList:function(){var t=Object(i["a"])(regeneratorRuntime.mark((function t(){var e,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return e={type:this.currentTabId,page:{pageNumber:this.pageNumber,pageSize:this.pageSize}},this.$store.state.sys.showLoading=!0,t.next=4,this.$net.post("address/getList",e);case 4:s=t.sent,this.$store.state.sys.showLoading=!1,s.success?(this.addressList=this.addressList.concat(s.data.list),this.isLastPage=s.data.isLastPage):this.$my_message(s.message);case 7:case"end":return t.stop()}}),t,this)})));function e(){return t.apply(this,arguments)}return e}(),e_goAddAddress:function(){wx.navigateTo({url:"/pages/WriteTheAddress/main"})},e_selectAddress:function(){},e_deleteAddress:function(t,e){var s=this;o["a"].confirm({title:"删除地址",message:"确认删除该地址吗？"}).then(Object(i["a"])(regeneratorRuntime.mark((function n(){var a,i;return regeneratorRuntime.wrap((function(n){while(1)switch(n.prev=n.next){case 0:return a={id:t},s.$store.state.sys.showLoading=!0,n.next=4,s.$net.post("address/delete",a);case 4:i=n.sent,s.$store.state.sys.showLoading=!1,i.success?(wx.showToast({title:"删除成功",icon:"none"}),s.addressList.splice(e,1)):wx.showToast({title:i.message,icon:"none"});case 7:case"end":return n.stop()}}),n)})))).catch((function(){}))},e_editAddress:function(t){wx.navigateTo({url:"/pages/WriteTheAddress/main?editParams=".concat(JSON.stringify(t))})}},mounted:function(){},created:function(){}},d=c,u=(s("2395"),s("2877")),l=Object(u["a"])(d,n,a,!1,null,"59002777",null);e["default"]=l.exports},afd0:function(t,e,s){"use strict";var n=s("59e6"),a=s.n(n);a.a},c10c:function(t,e,s){"use strict";s.r(e);var n=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"VersionInformation"},[s("BaseNavbar",{attrs:{title:"关于我们",isLeft:t.isLeft}}),t._m(0),s("div",{staticClass:"VI_version"},[s("h5",[t._v("V"+t._s(t.versionName))])]),s("div",{staticClass:"VI_introduce"},[s("div",{staticClass:"VIv_content",domProps:{innerHTML:t._s(t.divInfo)}}),t.vi_error?s("span",{staticClass:"UA_error"},[t._v("暂无数据")]):t._e()]),s("div",{staticClass:"VI_ThePhone"},[s("div",{staticClass:"VITP"},[s("span",{staticClass:"VITP_left"},[t._v("客服电话")]),s("span",{staticClass:"VITP_right",on:{click:t.myphone}},[t._v("4000093149")])])]),t._m(1)],1)},a=[function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"vI_logo"},[s("img",{attrs:{src:"http://xeryb.oss-cn-qingdao.aliyuncs.com/hexueuri/gong/xylogo1024.png"}})])},function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"VI_update"},[s("div",{staticClass:"VIu_check"},[s("span",{staticClass:"VIu_left"},[t._v("检查更新")]),s("span",{staticClass:"VIu_right"},[t._v("当前已是最新版本")])])])}],i=(s("4917"),s("96cf"),s("3b8d")),r=s("15ac"),o={data:function(){return{isLeft:!0,vi_error:!1,divInfo:"",versionName:""}},components:{BaseNavbar:r["a"]},computed:{},methods:{getVersionInformation:function(){var t=Object(i["a"])(regeneratorRuntime.mark((function t(){var e,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return e="0",this.$store.state.sys.showLoading=!0,t.next=4,this.$net.post("agreement/getAgreement",e);case 4:s=t.sent,this.$store.state.sys.showLoading=!1,1==s.success?this.divInfo=s.data.content:(this.vi_error=!0,this.$my_message("".concat(s.message)));case 7:case"end":return t.stop()}}),t,this)})));function e(){return t.apply(this,arguments)}return e}(),getVersion:function(){var t=Object(i["a"])(regeneratorRuntime.mark((function t(){var e,s,n,a,i;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return e=navigator.userAgent,navigator.appVersion,s=e.indexOf("Android")>-1||e.indexOf("Linux")>-1,!!e.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),n="",n=s?"1":"0",a={terminalId:n,groupId:"0"},t.next=8,this.$net.post("config/getVersion",a);case 8:i=t.sent,1==i.success?i.data.versionName?this.versionName=i.data.versionName:this.versionName="获取版本错误":this.$my_message("".concat(i.message));case 10:case"end":return t.stop()}}),t,this)})));function e(){return t.apply(this,arguments)}return e}(),myphone:function(){}},mounted:function(){this.getVersionInformation(),this.getVersion()},created:function(){}},c=o,d=(s("da7f"),s("2877")),u=Object(d["a"])(c,n,a,!1,null,"266f9096",null);e["default"]=u.exports},da7f:function(t,e,s){"use strict";var n=s("577d"),a=s.n(n);a.a}}]);