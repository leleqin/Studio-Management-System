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
      title="新建工作室"
      @on-ok="newOk('workspaceNew')"
      @on-cancel="cancel()"
    >
      <Form ref="workspaceNew" :model="workspaceNew" :rules="ruleNew" :label-width="80">
        <Row>
          <Col span="12">
            <Form-item label="名称:" prop="name">
              <Input v-model="workspaceNew.name" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="地址:" prop="address">
              <Input v-model="workspaceNew.address" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="编号:" prop="uid">
              <Input v-model="workspaceNew.uid" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="管理人:" prop="userId">
              <!-- <Input v-model="workspaceNew.userId" style="width: 204px" /> -->
              <Select v-model="workspaceNew" filterable clearable style="width: 200px">
                <Option
                  v-for="item in workspaceList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.label }}</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="创建时间:" prop="createtime">
              <Input v-model="workspaceNew.createtime" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Form-item label="描述:" prop="desc">
          <Input v-model="workspaceNew.desc" type="textarea" :autosize="{minRows: 2,maxRows: 5}"></Input>
        </Form-item>
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
      @on-ok="modifyOk('workspaceModify')"
      @on-cancel="cancel()"
    >
      <Form ref="workspaceModify" :model="workspaceModify" :rules="ruleModify" :label-width="80">
        <Row>
          <Col span="12">
            <Form-item label="名称:" prop="name">
              <Input v-model="workspaceModify.name" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="地址:" prop="address">
              <Input v-model="workspaceModify.address" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="编号:" prop="uid">
              <Input v-model="workspaceModify.uid" style="width: 204px" />
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="管理人:" prop="userId">
              <Input v-model="workspaceModify.userId" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="创建时间:" prop="createtime">
              <Input v-model="workspaceModify.createtime" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Form-item label="描述:" prop="desc">
          <Input v-model="workspaceModify.desc" type="textarea" :autosize="{minRows: 2,maxRows: 5}"></Input>
        </Form-item>
      </Form>
    </Modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      /*用于查找的菜单id*/
      workspaceId: null,
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
      /*workspace实体*/
      workspace: {
        id: null,
        name: null,
        desc: null,
        address: null,
        uid: null,
        userId: null,
        createtime: null
      },
      /*用于添加的workspace实体*/
      workspaceNew: {
        id: null,
        name: null,
        desc: null,
        address: null,
        uid: null,
        userId: null,
        createtime: null
      },
      /*用于修改的workspace实体*/
      workspaceModify: {
        id: null,
        name: null,
        desc: null,
        address: null,
        uid: null,
        userId: null,
        createtime: null
      },
      /*新建验证*/
      ruleNew: {
        name: [
          {
            type: "string",
            required: true,
            message: "输入工作室名称",
            trigger: "blur"
          }
        ],
        address: [
          {
            type: "string",
            required: true,
            message: "输入工作室地址",
            trigger: "blur"
          }
        ],
        uid: [
          { required: true, message: "输入工作室编号", trigger: "blur" },
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
        userId: [
          {
            type: "string",
            required: true,
            message: "输入管理人",
            trigger: "blur"
          }
        ],
        createtime: [
          {
            type: "string",
            required: true,
            message: "输入时间",
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
            message: "输入工作室名称",
            trigger: "blur"
          }
        ],
        address: [
          {
            type: "string",
            required: true,
            message: "输入工作室地址",
            trigger: "blur"
          }
        ],
        uid: [
          { required: true, message: "输入工作室编号", trigger: "blur" },
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
        userId: [
          {
            type: "string",
            required: true,
            message: "输入管理人",
            trigger: "blur"
          }
        ],
        createtime: [
          {
            type: "string",
            required: true,
            message: "输入时间",
            trigger: "blur"
          }
        ]
      },
      /*菜单列表*/
      workspaceList: [],
      /*生产类型表显示字段*/
      columns1: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "工作室ID",
          key: "id"
        },
        {
          title: "工作室名称",
          key: "name"
        },
        {
          title: "工作室简介",
          key: "desc"
        },
        {
          title: "地址",
          key: "address"
        },
        {
          title: "工作室编号",
          key: "uid"
        },
        {
          title: "管理人",
          key: "userId"
        },
        {
          title: "创建时间",
          key: "create_time"
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
      workspaceId: this.workspaceId
    });
    this.axios({
      method: "get",
      url: "/menus",
      params: {
        page: e.pageInfo.page,
        pageSize: e.pageInfo.pageSize,
        menuId: e.menuId
      }
    })
      .then(
        function(response) {
          var listTemp = response.data.data;
          for (var i = 0; i < listTemp.length; i++) {
            this.workspaceList.push({
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
    /*workspace实体初始化*/
    initWorkspace() {
      this.workspace.id = null;
      this.workspace.name = null;
      this.workspace.desc = null;
      this.workspace.address = null;
      this.workspace.uid = null;
      this.workspace.userId = null;
      this.workspace.createtime = null;
    },
    /*workspaceNew实体初始化*/
    initWorkspaceNew() {
      this.workspaceNew.id = null;
      this.workspaceNew.name = null;
      this.workspaceNew.desc = null;
      this.workspaceNew.address = null;
      this.workspaceNew.uid = null;
      this.workspaceNew.userId = null;
      this.workspaceNew.createtime = null;
    },
    /*workspaceModify实体初始化*/
    initWorkspaceModify() {
      this.workspaceModify.id = null;
      this.workspaceModify.name = null;
      this.workspaceModify.desc = null;
      this.workspaceModify.address = null;
      this.workspaceModify.uid = null;
      this.workspaceModify.userId = null;
      this.workspaceModify.createtime = null;
    },
    /*workspaceNew设置*/
    workspaceSet(e) {
      this.workspace.id = e.id;
      this.workspace.name = e.name;
      this.workspace.desc = e.desc;
      this.workspace.address = e.address;
      this.workspace.uid = e.uid;
      this.workspace.userId = e.userId;
      this.workspace.createtime = e.createtime;
    },
    /*workspaceNew设置*/
    workspaceNewSet(e) {
      this.workspaceNew.id = e.id;
      this.workspaceNew.name = e.name;
      this.workspaceNew.desc = e.desc;
      this.workspaceNew.address = e.address;
      this.workspaceNew.uid = e.uid;
      this.workspaceNew.userId = e.userId;
      this.workspaceNew.createtime = e.createtime;
    },
    /*workspaceModify设置*/
    workspaceModifySet(e) {
      this.workspaceModify.id = e.id;
      this.workspaceModify.name = e.name;
      this.workspaceModify.desc = e.desc;
      this.workspaceModify.address = e.address;
      this.workspaceModify.uid = e.uid;
      this.workspaceModify.userId = e.userId;
      this.workspaceModify.createtime = e.createtime;
    },
    /*得到表数据*/
    getTable(e) {
      this.axios({
        method: "get",
        url: "/workspaces",
        params: {
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize,
          workspaceId: this.workspaceId
        }
      })
        .then(
          function(response) {
            this.data1 = response.data.data.data;
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
        workspaceId: this.workspaceId
      });
    },
    /*分页点击事件*/
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getTable({
        pageInfo: this.pageInfo,
        workspaceId: this.workspaceId
      });
    },
    /*新建点击触发事件*/
    openNewModal() {
      this.newModal = true;
      this.initWorkspaceNew();
      this.count = 0;
      this.groupId = null;
    },
    /*新建modal的newOk点击事件*/
    newOk(workspaceNew) {
      this.$refs[workspaceNew].validate(valid => {
        if (valid) {
          this.initWorkspace();
          this.workspaceSet(this.workspaceNew);
          this.axios({
            method: "post",
            url: "/workspaces/workspace",
            data: this.workspace
          })
            .then(
              function(response) {
                this.initWorkspaceNew();
                this.getTable({
                  pageInfo: this.pageInfo,
                  workspaceId: this.workspaceId
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
    modifyOk(workspaceModify) {
      this.$refs[workspaceModify].validate(valid => {
        if (valid) {
          this.initWorkspace();
          this.workspaceSet(this.workspaceModify);
          this.axios({
            method: "put",
            url: "/workspaces/" + this.workspace.id,
            data: this.workspace
          })
            .then(
              function(response) {
                this.initWorkspaceNew();
                this.getTable({
                  pageInfo: this.pageInfo,
                  workspaceId: this.workspaceId
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
        this.workspaceModifySet(e["0"]);
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
          url: "/workspaces",
          data: this.groupId
        })
          .then(
            function(response) {
              this.getTable({
                pageInfo: this.pageInfo,
                workspaceId: this.workspaceId
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
      this.workspaceModifySet(e);
      this.modifyModal = true;
      this.data1.sort();
    }
  }
};
</script>
