<style scoped>
.operation-button {
  margin-right: 3px;
}
</style>
<template>
  <div style="margin: 20px;">
    <div>
      <Row style="margin-bottom: 25px;">
        <Col span="8">
          用户名：
          <Input v-model="name" placeholder="请输入..." style="width:200px" />
        </Col>
        <Col span="8">
          <Button type="primary" shape="circle" icon="ios-search" @click="search()">搜索</Button>
        </Col>
      </Row>
    </div>
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
    <!-- 添加modal -->
    <Modal
      :mask-closable="false"
      :visible.sync="newModal"
      v-model="newModal"
      :loading="loading"
      width="600"
      title="添加"
      @on-ok="newOk('userNew')"
      @on-cancel="cancel()"
    >
      <Form ref="userNew" :model="userNew" :rules="ruleNew" :label-width="100">
        <Row>
          <Col span="12">
            <Form-item label="用户名:" prop="name">
              <Input v-model="userNew.name" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="登录名:" prop="loginName">
              <Input v-model="userNew.loginName" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="登录密码:" prop="password">
              <Input v-model="userNew.password" style="width: 204px" type="password" password />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="再次输入密码:" prop="passwordAgain">
              <Input v-model="userNew.passwordAgain" style="width: 204px" type="password" password />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="邮箱:" prop="email">
              <Input v-model="userNew.email" style="width: 204px" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="用户类型:" prop="usertype">
              <Select v-model="userNew.usertype" style="width:200px">
                <Option :value="0">普通用户</Option>
                <Option :value="1">管理员</Option>
              </Select>
              <!-- <Input v-model="userModify.email" style="width: 204px"/> -->
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="工作室名称:" prop="workspaceId">
              <Select v-model="userNew.workspaceId" filterable clearable style="width: 200px">
                <Option
                  v-for="item in workspaceList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.label }}</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
      </Form>
    </Modal>
    <!--修改modal-->
    <Modal
      :mask-closable="false"
      :visible.sync="modifyModal"
      v-model="modifyModal"
      width="600"
      title="修改"
      @on-ok="modifyOk('modifyModal')"
      @on-cancel="cancel()"
    >
      <Form :label-width="100">
        <Row>
          <Col span="12">
            <Form-item label="登录名:">
              <Input v-model="userModify.name" style="width: 204px" disabled="disabled" />
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="用户类型:">
              <Select v-model="userModify.usertype" style="width:200px">
                <Option :value="0">普通用户</Option>
                <Option :value="1">管理员</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="12">
            <Form-item label="所属工作室:">
              <Select v-model="userModify.workspaceId" style="width:200px">
                <Option
                  v-for="item in workspaceList"
                  :value="item.value"
                  :key="item.value"
                >{{ item.label }}</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
      </Form>
    </Modal>
    <!--配置角色modal-->
    <Modal v-model="roleModal" width="500" title="角色配置" @on-ok="roleOk()" @on-cancel="cancel()">
      <div>
        <Table
          border
          :columns="columns2"
          :data="data2"
          :height="260"
          @on-selection-change="s=>{change2(s)}"
        ></Table>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  data() {
    return {
      /*用于查找的登录名*/
      name: null,
      /*选择的数量*/
      count: null,
      /*选中的组数据*/
      groupId: null,
      /*新建modal的显示参数*/
      newModal: false,
      /*修改modal的显示参数*/
      modifyModal: false,
      /*角色配置modal的显示参数*/
      roleModal: false,
      /*分页total属性绑定值*/
      total: 0,
      /*loading*/
      loading: true,
      /*用于查找的工作室ID*/
      workspaceId: null,
      /*pageInfo实体*/
      pageInfo: {
        page: 0,
        pageSize: 10
      },
      /*user实体*/
      user: {
        id: null,
        name: null,
        loginName: null,
        password: null,
        passwordAgain: null,
        email: null,
        usertype: null,
        workspaceId: null
      },
      /*用于添加的user实体*/
      userNew: {
        id: null,
        name: null,
        loginName: null,
        password: null,
        passwordAgain: null,
        email: null,
        usertype: null,
        workspaceId: null
      },
      /*用于修改的user实体*/
      userModify: {
        id: null,
        name: null,
        usertype: null,
        workspaceId: null
      },
      /*新建验证*/
      ruleNew: {
        name: [
          {
            type: "string",
            required: true,
            message: "输入用户名",
            trigger: "blur"
          }
        ],
        loginName: [
          {
            type: "string",
            required: true,
            message: "输入登录名",
            trigger: "blur"
          }
        ],
        password: [
          {
            type: "string",
            required: true,
            message: "输入密码",
            trigger: "blur"
          }
        ],
        passwordAgain: [
          {
            type: "string",
            required: true,
            message: "输入再次密码",
            trigger: "blur"
          }
        ],
        email: [
          { required: true, message: "输入邮箱", trigger: "blur" },
          { type: "email", message: "输入正确的邮箱格式", trigger: "blur" }
        ]
      },
      /*修改验证*/
      ruleModify: {
        name: [
          {
            type: "string",
            required: true,
            message: "输入用户名",
            trigger: "blur"
          }
        ],
        password: [
          {
            type: "string",
            required: true,
            message: "输入密码",
            trigger: "blur"
          }
        ],
        email: [
          { required: true, message: "输入邮箱", trigger: "blur" },
          { type: "email", message: "输入正确的邮箱格式", trigger: "blur" }
        ]
      },
      /*表显示字段*/
      columns1: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "用户名",
          key: "name"
        },
        {
          title: "头像",
          key: "headimg",
          width: 80,
          align: "center",
          render: (h, params) => {
            return h("img", {
              attrs: {
                src: params.row.headimg
              },
              style: {
                width: "30px",
                height: "30px"
              }
            });
          }
        },
        {
          title: "url",
          key: "url",
          width: 230,
          render: (h, params) => {
            return h(
              "a",
              {
                attrs: {
                  href: params.row.url,
                  target: "_blank"
                }
              },
              params.row.url
            );
          }
        },
        {
          title: "邮箱",
          key: "email"
        },
        {
          title: "用户类型",
          align: "center",
          key: "usertype",
          render: (h, params) => {
            if (params.row.usertype == 0) {
              return h("div", [h("strong", null, "普通用户")]);
            } else if (params.row.usertype == 1) {
              return h("div", [h("strong", null, "管理员")]);
            }
          }
        },
        {
          title: "所属工作室",
          key: "workspaceName"
        },
        {
          title: "注册时间",
          key: "createTime"
        },
        {
          title: "操作",
          align: "center",
          key: "action",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "info"
                  },
                  on: {
                    click: () => {
                      this.relationSet(params.row);
                    }
                  }
                },
                "配置角色"
              )
            ]);
          }
        }
      ],
      /*菜单列表*/
      workspaceList: [],
      /*表数据*/
      data1: [],
      /*表显示字段*/
      columns2: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "角色名称",
          width: 120,
          key: "name"
        },
        {
          title: "描述",
          key: "describe"
        }
      ],
      /*表数据*/
      data2: [],
      /*data2的临时存储*/
      data2Temp: [],
      /*用户与角色关系列表*/
      relationList: null
    };
  },
  mounted() {
    /*页面初始化调用方法*/
    this.getTable({
      pageInfo: this.pageInfo,
      name: this.name
    });
    this.getWorkspace({
      pageInfo: this.pageInfo,
      workspaceId: this.workspaceId
    });
    this.axios({
      method: "get",
      url: "/roles/all"
    })
      .then(
        function(response) {
          this.data2Temp = response.data.data;
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
    /*user实体初始化*/
    initUser() {
      this.user.id = null;
      this.user.name = null;
      this.user.loginName = null;
      this.user.password = null;
      this.user.email = null;
      this.user.usertype = null;
      this.user.workspaceId = null;
    },
    /*userNew实体初始化*/
    initUserNew() {
      this.userNew.id = null;
      this.userNew.name = null;
      this.userNew.loginName = null;
      this.userNew.password = null;
      this.userNew.passwordAgain = null;
      this.userNew.email = null;
      this.userNew.usertype = null;
      this.userNew.workspaceId = null;
    },
    /*userModify实体初始化*/
    initUserModify() {
      this.userModify.id = null;
      this.userModify.name = null;
      this.userModify.loginName = null;
      this.userModify.password = null;
      this.userModify.email = null;
      this.userModify.workspaceId = null;
    },
    /*userNew设置*/
    userSet(e) {
      this.user.id = e.id;
      this.user.name = e.name;
      this.user.loginName = e.loginName;
      this.user.password = e.password;
      this.user.email = e.email;
      this.user.usertype = e.usertype;
      this.user.workspaceId = e.workspaceId;
    },
    /*userNew设置*/
    userNewSet(e) {
      this.userNew.id = e.id;
      this.userNew.name = e.name;
      this.userNew.loginName = e.loginName;
      this.userNew.password = e.password;
      this.userNew.passwordAgain = e.passwordAgain;
      this.userNew.email = e.email;
      this.userNew.usertype = e.usertype;
      this.userNew.workspaceId = e.workspaceId;
    },
    /*userModify设置*/
    userModifySet(e) {
      this.userModify.id = e.id;
      this.userModify.name = e.name;
      this.userModify.usertype = e.usertype;
      this.userModify.workspaceId = e.workspaceId;
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
        url: "/users",
        params: {
          page: e.pageInfo.page,
          pageSize: e.pageInfo.pageSize,
          name: e.name
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
          console.log(error);
        });
    },
    getWorkspace(e) {
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
            var listTemp = response.data.data.data;
            console.log(listTemp);
            for (var i = 0; i < listTemp.length; i++) {
              this.workspaceList.push({
                value: listTemp[i].id,
                label: listTemp[i].name
              });
            }
          }.bind(this)
        )
        .catch(function(error) {
          console.log(error);
        });
    },
    /*搜索按钮点击事件*/
    search() {
      this.initPageInfo();
      this.getTable({
        pageInfo: this.pageInfo,
        name: this.name
      });
    },
    /*分页点击事件*/
    pageSearch(e) {
      this.pageInfo.page = e - 1;
      this.getTable({
        pageInfo: this.pageInfo,
        name: this.name
      });
    },
    /*新建点击触发事件*/
    openNewModal() {
      this.newModal = true;
      this.initUserNew();
      this.data1.sort();
      this.count = 0;
      this.groupId = null;
    },
    /*新建modal的newOk点击事件*/
    newOk(userNew) {
      this.$refs[userNew].validate(valid => {
        if (valid) {
          if (this.userNew.password == this.userNew.passwordAgain) {
            this.initUser();
            this.userSet(this.userNew);
            this.axios({
              method: "post",
              url: "/register",
              data: this.user
            })
              .then(
                function(response) {
                  this.initUserNew();
                  this.getTable({
                    pageInfo: this.pageInfo,
                    name: this.name
                  });
                  this.$Message.info("新建成功");
                }.bind(this)
              )
              .catch(function(error) {
                alert("该用户已注册");
              });
            this.newModal = false;
          } else {
            this.$Message.error("两次输入的密码不相同！");
            this.loading = false;
            this.$nextTick(() => {
              this.loading = true;
            });
          }
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
    modifyOk() {
      // this.initUser();
      // this.userSet(this.userModify);
      this.axios({
        method: "put",
        url: "/users/user",
        data: {
          name: this.userModify.name,
          usertype: this.userModify.usertype,
          id: this.userModify.id,
          workspaceId: this.userModify.workspaceId
        }
      })
        .then(
          function(response) {
            this.initUserNew();
            this.getTable({
              pageInfo: this.pageInfo,
              name: this.name
            });
            this.$Message.info("修改成功");
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
      this.modifyModal = false;
    },
    /*modal的cancel点击事件*/
    cancel() {
      this.$Message.info("点击了取消");
    },
    /*table选择后触发事件*/
    change(e) {
      if (e.length == 1) {
        this.userModifySet(e["0"]);
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
          url: "/users",
          data: this.groupId
        })
          .then(
            function(response) {
              this.getTable({
                pageInfo: this.pageInfo,
                name: this.name
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
      this.userModifySet(e);
      this.modifyModal = true;
      this.data1.sort();
    },
    /*流程配置*/
    relationSet(e) {
      this.roleModal = true;
      this.data2 = [];
      this.axios({
        method: "get",
        url: "/relations/" + e.id
      })
        .then(
          function(response) {
            var roleList = [];
            for (var i in response.data.data) {
              roleList.push(response.data.data[i].roleId);
            }
            for (var i in this.data2Temp) {
              if (roleList.indexOf(this.data2Temp[i].id) == -1) {
                this.data2.push({
                  id: this.data2Temp[i].id,
                  name: this.data2Temp[i].name,
                  describe: this.data2Temp[i].describe,
                  userId: e.id,
                  _checked: false
                });
              } else {
                this.data2.push({
                  id: this.data2Temp[i].id,
                  name: this.data2Temp[i].name,
                  describe: this.data2Temp[i].describe,
                  userId: e.id,
                  _checked: true
                });
              }
            }
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    },
    /*配置角色modal确认按钮点击事件*/
    roleOk() {
      if (this.relationList != null) {
        this.axios({
          method: "post",
          url: "/relations",
          data: this.relationList
        })
          .then(
            function(response) {
              this.$Message.info("配置成功");
            }.bind(this)
          )
          .catch(function(error) {
            alert(error);
          });
        this.relationList = null;
      }
    },
    /*配置角色modal中表选择改变事件*/
    change2(e) {
      this.relationList = [];
      if (e.length == 0) {
        this.relationList.push({
          userId: this.data2[0].userId
        });
      }
      for (var i in e) {
        this.relationList.push({
          userId: e[i].userId,
          roleId: e[i].id
        });
      }
    }
  }
};
</script>
