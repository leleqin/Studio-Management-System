<style>
.del-select {
  margin-bottom: 25px;
}
.del-select span {
  margin-right: 5px;
}
.botton-layout {
  margin-right: 5px;
}
</style>
<template>
  <div style="margin: 40px;">
    <div>
      <Row style="margin-bottom: 25px;">
        <Col span="9">
          搜索：
          <Input v-model="searchContent" placeholder="请输入..." style="width:300px" />
        </Col>
        <Col span="6">
          <!-- v-model="date" -->
          <DatePicker
            @on-change="handleChange"
            type="date"
            placeholder="Select date"
            style="width: 200px"
          ></DatePicker>
        </Col>
        <Col span="4">
          <Button type="primary" shape="circle" icon="ios-search" @click="search()">搜索</Button>
        </Col>
      </Row>
    </div>
    <div>
      <ul>
        <li>
          <div style="padding: 10px 0;">
            <Table
              border
              :columns="columns1"
              :data="data1"
              :height="400"
              @on-selection-change="s=>{change(s)}"
            ></Table>
          </div>
        </li>
        <li>
          <div style="text-align: right;">
            <Page
              :total="total"
              :page-size="pageInfo.pageSize"
              show-elevator
              show-total
              @on-change="e=>{pageSearch(e)}"
            ></Page>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      date: null,
      searchContent: null,
      groupId: [],
      /*分页total属性绑定值*/
      total: 0,
      /*pageInfo实体*/
      pageInfo: {
        page: 0,
        pageSize: 10
      },
      /*表显示字段*/
      columns1: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "学生姓名",
          key: "name"
        },
        {
          title: "签到时间",
          key: "createTime"
        },
        {
          title: "所属工作室",
          key: "workspaceName"
        }
      ],
      /*表数据*/
      data1: []
    };
  },
  mounted() {
    /*页面初始化调用方法*/
    this.getTable({
      pageInfo: this.pageInfo
    });
  },
  methods: {
    handleChange(daterange) {
      this.date = daterange;
    },
    /*pageInfo实体初始化*/
    initPageInfo() {
      this.pageInfo.page = 0;
      this.pageInfo.pageSize = 10;
    },
    /*得到表数据*/
    getTable(e) {
      var dateTimestamp = null;
      if (this.date != null && this.date != "") {
        dateTimestamp = this.date;
      }
      this.axios({
        method: "get",
        url: "/sign/attendance",
        params: {
          dateTimestamp: dateTimestamp,
          searchContent: this.searchContent,
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize
        }
      })
        .then(
          function(response) {
            this.data1 = response.data.data.data;
            this.total = response.data.data.totalCount;
            for (var i = this.data1.length - 1; i >= 0; i--) {
              console.log("date:" + this.data1[i].createTime);
              this.data1[i].createTime = this.dateGet(this.data1[i].createTime);
            }
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    },
    search() {
      this.initPageInfo();
      this.getTable({
        pageInfo: this.pageInfo
      });
    },
    /*分页点击事件*/
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getTable({
        pageInfo: this.pageInfo
      });
    },
    change(e) {
      this.setGroupId(e);
    },
    setGroupId(e) {
      this.groupId = [];
      for (var i = 0; i <= e.length - 1; i++) {
        this.groupId.push(e[i].id);
      }
    },
    switchChange() {
      this.groupId = [];
      this.getTable({
        pageInfo: this.pageInfo
      });
    }
  }
};
</script>
