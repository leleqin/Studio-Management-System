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
    <!--添加modal-->
    <Modal
      :mask-closable="false"
      :visible.sync="newModal"
      :loading="loading"
      v-model="newModal"
      width="600"
      title="新建工作室财产统计"
      @on-ok="newOk('assetsNew')"
      @on-cancel="cancel()"
    >
      <Form ref="assetsNew" :model="assetsNew" :rules="ruleNew" :label-width="80">
        <Row>
          <Col span="12">
            <Form-item label="名称:" prop="name">
              <Input v-model="assetsNew.name" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="设备类型:" prop="type">
              <Input v-model="assetsNew.type" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="设备数量:" prop="number">
              <Input v-model="assetsNew.number" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="管理人:" prop="userId">
              <Select v-model="assetsNew.userId" filterable clearable style="width: 200px">
                <Option
                  v-for="item in assetsList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.label }}</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="损坏程度:" prop="abrasion">
              <Input v-model="assetsNew.abrasion" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
      </Form>
    </Modal>
    <!--修改modal-->
    <Modal
      :mask-closable="false"
      :visible.sync="modifyModal"
      :loading="loading"
      v-model="modifyModal"
      width="600"
      title="修改"
      @on-ok="modifyOk('assetsModify')"
      @on-cancel="cancel()"
    >
      <Form ref="assetsModify" :model="assetsModify" :rules="ruleModify" :label-width="80">
        <Row>
          <Col span="12">
            <Form-item label="名称:" prop="name">
              <Input v-model="assetsModify.name" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="设备类型:" prop="type">
              <Input v-model="assetsModify.type" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="设备数量:" prop="number">
              <Input v-model="assetsModify.number" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="管理人:" prop="userId">
              <Select v-model="assetsModify.userId" filterable clearable style="width: 200px">
                <Option
                  v-for="item in assetsList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.label }}</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="损坏程度:" prop="abrasion">
              <Input v-model="assetsModify.abrasion" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
      </Form>
    </Modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      /*用于查找的菜单id*/
      assetsId: null,
      /*选择的数量*/
      count: null,
      /*选中的组数据*/
      groupId: null,
      /*新建modal的显示参数*/
      newModal: false,
      /*修改modal的显示参数*/
      modifyModal: false,
      /*分页total属性绑定值*/
      total: 0,
      /*loading*/
      loading: true,
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
        userId: null,
        createTime: null
      },
      /*用于添加的assets实体*/
      assetsNew: {
        id: null,
        name: null,
        type: null,
        number: null,
        abrasion: null,
        userId: null,
        createTime: null
      },
      /*用于修改的assets实体*/
      assetsModify: {
        id: null,
        name: null,
        type: null,
        number: null,
        abrasion: null,
        userId: null,
        createTime: null
      },
      /*新建验证*/
      ruleNew: {
        name: [
          {
            type: "string",
            required: true,
            message: "输入财产名称",
            trigger: "blur"
          }
        ],
        type: [
          {
            type: "string",
            required: true,
            message: "输入财产类型",
            trigger: "blur"
          }
        ],
        number: [
          { required: true, message: "输入财产数量", trigger: "blur" },
          {
            validator(rule, value, callback) {
              if (!Number.isInteger(+value)) {
                callback(new Error("请输入数字"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        abrasion: [
          { required: true, message: "输入损坏程度", trigger: "blur" },
          {
            validator(rule, value, callback) {
              if (!Number.isInteger(+value)) {
                callback(new Error("请输入数字"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ]
      },
      /*修改验证*/
      ruleModify: {
        name: [
          {
            type: "string",
            required: true,
            message: "输入财产名称",
            trigger: "blur"
          }
        ],
        type: [
          {
            type: "string",
            required: true,
            message: "输入财产类型",
            trigger: "blur"
          }
        ],
        number: [
          { required: true, message: "输入财产数量", trigger: "blur" },
          {
            validator(rule, value, callback) {
              if (!Number.isInteger(+value)) {
                callback(new Error("请输入数字"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        abrasion: [
          { required: true, message: "输入损坏程度", trigger: "blur" },
          {
            validator(rule, value, callback) {
              if (!Number.isInteger(+value)) {
                callback(new Error("请输入数字"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ]
      },
      /*菜单列表*/
      assetsList: [],
      /*生产类型表显示字段*/
      columns1: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "ID",
          key: "id"
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
          key: "userName"
        },
        {
          title: "创建时间",
          key: "createTime"
        }
      ],
      /*生产类型表数据*/
      data1: []
    };
  },
  mounted() {
    /*页面初始化调用方法*/
    this.getTable({
      pageInfo: this.pageInfo,
      assetsId: this.assetsId
    });
    this.axios({
      method: "get",
      url: "/getAdmin",
      params: {
        usertype: 1
      }
    })
      .then(
        function(response) {
          var listTemp = response.data.data;
          for (var i = 0; i < listTemp.length; i++) {
            this.assetsList.push({
              value: listTemp[i].id,
              label: listTemp[i].name
            });
          }
        }.bind(this)
      )
      .catch(function(error) {
        alert(error);
      });
  },
  methods: {
    /*pageInfo实体初始化*/
    initPageInfo() {
      this.pageInfo.page = 0;
      this.pageInfo.pageSize = 10;
    },
    /*assets实体初始化*/
    initAssets() {
      this.assets.id = null;
      this.assets.name = null;
      this.assets.type = null;
      this.assets.number = null;
      this.assets.abrasion = null;
      this.assets.userId = null;
      this.assets.createTime = null;
    },
    /*assetsNew实体初始化*/
    initAssetsNew() {
      this.assetsNew.id = null;
      this.assetsNew.name = null;
      this.assetsNew.type = null;
      this.assetsNew.number = null;
      this.assetsNew.abrasion = null;
      this.assetsNew.userId = null;
      this.assetsNew.createTime = null;
    },
    /*assetsModify实体初始化*/
    initAssetsModify() {
      this.assetsModify.id = null;
      this.assetsModify.name = null;
      this.assetsModify.type = null;
      this.assetsModify.number = null;
      this.assetsModify.abrasion = null;
      this.assetsModify.userId = null;
      this.assetsModify.createTime = null;
    },
    /*assetsNew设置*/
    assetsSet(e) {
      this.assets.id = e.id;
      this.assets.name = e.name;
      this.assets.type = e.type;
      this.assets.number = e.number;
      this.assets.abrasion = e.abrasion;
      this.assets.userId = e.userId;
      this.assets.createTime = e.createTime;
    },
    /*assetsNew设置*/
    assetsNewSet(e) {
      this.assetsNew.id = e.id;
      this.assetsNew.name = e.name;
      this.assetsNew.type = e.type;
      this.assetsNew.number = e.number;
      this.assetsNew.abrasion = e.abrasion;
      this.assetsNew.userId = e.userId;
      this.assetsNew.createTime = e.createTime;
    },
    /*assetsModify设置*/
    assetsModifySet(e) {
      this.assetsModify.id = e.id;
      this.assetsModify.name = e.name;
      this.assetsModify.type = e.type;
      this.assetsModify.number = e.number;
      this.assetsModify.abrasion = e.abrasion;
      this.assetsModify.userId = e.userId;
      this.assetsModify.createTime = e.createTime;
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
        e[i].createTime = this.dateGet(e[i].createTime);
      }
    },
    /*得到表数据*/
    getTable(e) {
      this.axios({
        method: "get",
        url: "/assets",
        params: {
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize,
          assetsId: this.assetsId
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
    /*搜索按钮点击事件*/
    search() {
      this.initPageInfo();
      this.getTable({
        pageInfo: this.pageInfo,
        assetsId: this.assetsId
      });
    },
    /*分页点击事件*/
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getTable({
        pageInfo: this.pageInfo,
        assetsId: this.assetsId
      });
    },
    /*新建点击触发事件*/
    openNewModal() {
      this.newModal = true;
      this.initAssetsNew();
      this.count = 0;
      this.groupId = null;
    },
    /*新建modal的newOk点击事件*/
    newOk(assetsNew) {
      this.$refs[assetsNew].validate(valid => {
        if (valid) {
          this.initAssets();
          this.assetsSet(this.assetsNew);
          this.axios({
            method: "post",
            url: "/assets/insertAssets",
            data: this.assets
          })
            .then(
              function(response) {
                this.initAssetsNew();
                this.getTable({
                  pageInfo: this.pageInfo,
                  assetsId: this.assetsId
                });
                this.$Message.info("新建成功");
              }.bind(this)
            )
            .catch(function(error) {
              alert(error);
            });
          this.newModal = false;
        } else {
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        }
      });
    },
    /*点击修改时判断是否只选择一个*/
    openModifyModal() {
      if (this.count > 1 || this.count < 1) {
        this.modifyModal = false;
        this.$Message.warning("请至少选择一项(且只能选择一项)");
      } else {
        this.modifyModal = true;
      }
    },
    /*修改modal的modifyOk点击事件*/
    modifyOk(assetsModify) {
      this.$refs[assetsModify].validate(valid => {
        if (valid) {
          this.initAssets();
          this.assetsSet(this.assetsModify);
          this.axios({
            method: "put",
            url: "/assets/" + this.assets.id,
            data: this.assets
          })
            .then(
              function(response) {
                this.initAssetsNew();
                this.getTable({
                  pageInfo: this.pageInfo,
                  assetsId: this.assetsId
                });
                this.$Message.info("修改成功");
              }.bind(this)
            )
            .catch(function(error) {
              alert(error);
            });
          this.modifyModal = false;
        } else {
          this.$Message.error("表单验证失败!");
          setTimeout(() => {
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }, 1000);
        }
      });
    },
    /*modal的cancel点击事件*/
    cancel() {
      this.$Message.info("点击了取消");
    },
    /*table选择后触发事件*/
    change(e) {
      if (e.length == 1) {
        this.assetsModifySet(e["0"]);
      }
      this.setGroupId(e);
    },
    /*通过选中的行设置groupId的值*/
    setGroupId(e) {
      this.groupId = [];
      this.count = e.length;
      for (var i = 0; i <= e.length - 1; i++) {
        this.groupId.push(e[i].id);
      }
    },
    /*删除table中选中的数据*/
    del() {
      if (this.groupId != null && this.groupId != "") {
        this.axios({
          method: "delete",
          url: "/assets",
          data: this.groupId
        })
          .then(
            function(response) {
              this.getTable({
                pageInfo: this.pageInfo,
                assetsId: this.assetsId
              });
              this.groupId = null;
              this.count = 0;
              this.$Message.info("删除成功");
            }.bind(this)
          )
          .catch(function(error) {
            alert(error);
          });
      }
    },
    /*表格中双击事件*/
    dblclick(e) {
      this.assetsModifySet(e);
      this.modifyModal = true;
      this.data1.sort();
    }
  }
};
</script>
