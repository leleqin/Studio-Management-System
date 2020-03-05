<style scoped>
.operation-button {
  margin-right: 3px;
}
</style>
<template>
  <div style="margin: 20px;">
    <div>
      <ul>
        <li>
          <Button class="operation-button" type="primary" icon="md-add" @click="openNewModal()">新建</Button>
          <Button
            class="operation-button"
            type="success"
            icon="md-build"
            @click="openModifyModal()"
          >修改</Button>
          <Button type="error" icon="md-trash" @click="del()">删除</Button>
        </li>
        <li>
          <div style="padding: 10px 0;">
            <Table
              border
              :columns="columns1"
              :data="data1"
              :height="400"
              @on-selection-change="s=>{change(s)}"
              @on-row-dblclick="s=>{dblclick(s)}"
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
    <Modal :mask-closable="false" :visible.sync="modal" v-model="modal" width="600" title="查看">
      <Form :label-width="80">
        <Form-item label="用户名:">
          <strong>{{assets.username}}</strong>
          <!-- <Input v-model="assets.username" style="width: 204px" disabled="disabled" /> -->
        </Form-item>
        <Form-item label="内容:">
          <span>{{assets.content}}</span>
          <!-- <Input v-model="assets.username" style="width: 204px" disabled="disabled" /> -->
        </Form-item>
      </Form>
      <div slot="footer">
        <Button type="error" size="large" @click="cancel">关闭</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      /*修改modal的显示参数*/
      modal: false,
      /*分页total属性绑定值*/
      total: 0,
      /*pageInfo实体*/
      pageInfo: {
        page: 0,
        pageSize: 10
      },
      /*assets实体*/
      assets: {
        id: null,
        name: null,
        type: null,
        number: null,
        abrasion: null,
        userid: null,
        createtime: null
      },
      /*表显示字段*/
      columns1: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "设备名",
          key: "name"
        },
        {
          title: "设备类型",
          key: "type"
        },
        {
          title: "设备数量",
          key: "number"
        },
        {
          title: "损坏程度",
          key: "abrasion"
        },
        {
          title: "管理人",
          key: "userid"
        },
        {
          title: "创建时间",
          key: "createtime"
        },
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
    /*pageInfo实体初始化*/
    initPageInfo() {
      this.pageInfo.page = 0;
      this.pageInfo.pageSize = 10;
    },
    assetsSet(e) {
      this.assets.id = e.id;
      this.assets.name = e.name;
      this.assets.type = e.type;
      this.assets.number = e.number;
      this.assets.abrasion = e.abrasion;
      this.assets.userid = e.userid;
      this.assets.createtime = e.createtime;
    },
    dateGet(e) {
      var time = new Date(parseInt(e));
      return (
        time.getFullYear() +
        "-" +
        (time.getMonth() + 1) +
        "-" +
        time.getDate() +
        " " +
        time.getHours() +
        ":" +
        time.getMinutes()
      );
    },
    listDateSet(e) {
      for (var i = e.length - 1; i >= 0; i--) {
        e[i].createtime = this.dateGet(e[i].createtime);
      }
    },
    /*得到表数据*/
    getTable(e) {
      this.axios({
        method: "get",
        url: "/assets",
        params: {
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize
        }
      })
        .then(
          function(response) {
            this.data1 = response.data.data.data;
            this.listDateSet(this.data1);
            this.total = response.data.data.totalCount;
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    },
    /*分页点击事件*/
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getTable({
        pageInfo: this.pageInfo
      });
    },
    /*新建点击触发事件*/
    openNewModal() {
      this.newModal = true;
      this.initMenuNew();
      this.data1.sort();
      this.count = 0;
      this.groupId = null;
    },
    /*modal的cancel点击事件*/
    cancel() {
      this.modal = false;
    },
    /*表格中双击事件*/
    dblclick(e) {
      this.assetsSet(e);
      this.modal = true;
    },
    assetsInfo(e) {
      this.assetsSet(e);
      this.modal = true;
    },
    del() {
      if (this.groupId != null && this.groupId != "") {
        this.axios({
          method: "delete",
          url: "/admin/emails",
          data: this.groupId
        })
          .then(
            function(response) {
              this.getTable({
                pageInfo: this.pageInfo
              });
              this.groupId = [];
              this.$Message.info("删除成功");
            }.bind(this)
          )
          .catch(function(error) {
            alert(error);
          });
      }
    },
    change(e) {
      this.setGroupId(e);
    },
    setGroupId(e) {
      this.groupId = [];
      for (var i = 0; i <= e.length - 1; i++) {
        this.groupId.push(e[i].id);
      }
    }
  }
};
</script>
