<template>
  <div class="home content-background">
    <Row :gutter="16" type="flex" justify="space-around">
      <Col span="17">
        <div style="background: #f5f7f9;padding-top:20px;padding-left:100px;">
          <div>
            <Carousel autoplay v-model="value2" loop>
              <CarouselItem v-for="(item,index) in bannerList" :key="index">
                <router-link :to="('/page/detail/'+item.id)">
                  <img :style="{height:'500px'}" class="images-con" v-bind:src="(item.image)" />
                </router-link>
              </CarouselItem>
            </Carousel>
          </div>
          <div
            v-if="flage"
            style="background: #f5f7f9;padding: 24px 50px;color: #495060;font-size: 14px;text-align: center;"
          >
            <span>未找到符合条件的结果</span>
          </div>
          <div class="box-flex flex-direction-column margin-top-2">
            <div
              class="box-flex margin-auto"
              style="width:100%;"
              v-for="(A,index) in homeArticle"
              :key="(A,index)"
            >
              <div class="box-flex width-100" v-if="index%2==0">
                <div class="flex-1">
                  <router-link :to="('/page/detail/'+A.id)">
                    <img class="images-con imgpic" v-bind:src="(A.image)" />
                  </router-link>
                </div>
                <div class="box-flex flex-1 info-padding-all flex-direction-column">
                  <router-link :to="('/page/detail/'+A.id)">
                    <span class="titleFont lineThrou">{{A.title}}</span>
                  </router-link>
                  <span class="contentFont">{{A.info}}</span>
                </div>
              </div>
              <div class="box-flex width-100" v-else style="padding-top:20px; padding-bottom:20px;">
                <div class="box-flex flex-1 info-padding-all flex-direction-column">
                  <router-link :to="('/page/detail/'+A.id)">
                    <span class="titleFont lineThrou">{{A.title}}</span>
                  </router-link>
                  <span class="contentFont">{{A.info}}</span>
                </div>
                <div class="flex-1">
                  <router-link :to="('/page/detail/'+A.id)">
                    <img class="images-con imgpic" v-bind:src="(A.image)" />
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Col>
      <Col span="7">
        <div
          style="background: #f5f7f9;;padding-left: 10px;padding-right: 100px;padding-top: 20px;"
        >
          <Card shadow>
            <p slot="title">No border title</p>
            <p>Content of no border type. Content of no border type. Content of no border type. Content of no border type.</p>
          </Card>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      flage: false,
      value2: 0,
      homeArticle: [],
      bannerList: []
    };
  },
  mounted() {
    this.getHomeArticle();
    this.getBanner();
  },
  watch: {
    $route: ["getHomeArticle"]
  },
  methods: {
    getBanner() {
      this.axios({
        method: "get",
        url: "/public/banners"
      })
        .then(
          function(response) {
            this.bannerList = response.data.data;
          }.bind(this)
        )
        .catch(
          function(error) {
            this.$Message.error("无权限");
          }.bind(this)
        );
    },
    getHomeArticle() {
      if (this.$route.params.title == null) {
        this.axios({
          method: "get",
          url: "/public/interests"
        })
          .then(
            function(response) {
              this.homeArticle = response.data.data;
            }.bind(this)
          )
          .catch(
            function(error) {
              this.$Message.error("无权限");
            }.bind(this)
          );
      } else {
        this.axios({
          method: "get",
          url: "/public/interests",
          params: {
            title: this.$route.params.title
          }
        })
          .then(
            function(response) {
              this.homeArticle = response.data.data;
              if (this.homeArticle.length == 0) {
                this.flage = true;
              } else {
                this.flage = false;
              }
            }.bind(this)
          )
          .catch(
            function(error) {
              this.$Message.error("无权限");
            }.bind(this)
          );
      }
    }
    // login(formLogin){
    //     this.$refs[formLogin].validate((valid) => {
    //         if(valid){
    //             this.$store.dispatch('users/userLogin',{"user_name":this.formLogin.userName,"user_password":this.formLogin.password,"router":this.$router});
    //         }
    //     })
    // }
  }
};
</script>
<style>
.content-background {
  background: #f5f7f9;
}
.info-padding-all {
  padding: 3%;
  background: #fff;
}
.home-content {
  background: #fff;
  padding-left: 120;
  padding-right: 20;
}
.titleFont {
  height: 40px;
  line-height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  font-size: 180%;
  color: #000;
}
.box-flex .imgpic {
  transition: 0.7s all;
  opacity: 0.8;
}

.box-flex .imgpic:hover {
  opacity: 1;
  box-shadow: 1px 1px 20px #333;
  transform: scale(1.1, 1.1);
  cursor: pointer;
}

.lineThrou {
  transition: 0.8s all;
}

.lineThrou:hover {
  cursor: pointer;
}
</style>
