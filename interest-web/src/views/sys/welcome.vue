<style scoped>
#welcome {
  width: 100%;
  height: 100%;
  background-size: cover;
  overflow: auto;
  background-position: center center;
  box-shadow: 0 0px 3px rgba(0, 0, 0, 0.5);
  text-align: center;
}
#welcome img {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>
<template>
  <div id="welcome">
    <!--<img src="../../images/welcome2.jpg" />-->
    <div>
      <br />
      <h1>文章统计</h1>
      <chart ref="chart1" :options="orgOptions" :auto-resize="true"></chart>
    </div>

    <!-- <div>
      签到统计
      <chart ref="chart2" :options="orgOptions2" :auto-resize="true"></chart>
    </div>-->
  </div>
</template>
<script>
export default {
  data() {
    return {
      orgOptions: {},
      orgOptions2: {},
      monthlyArticlesList: [],
      signInDate: [],
      signInTotal: []
    };
  },
  methods: {
    getData() {
      this.axios({
        method: "get",
        url: "/base/getMonthlyArticles"
      })
        .then(
          function(response) {
            let mapData = response.data.data;
            for (let value in mapData) {
              this.monthlyArticlesList.push(mapData[value]);
            }
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    },
    getData2() {
      this.axios({
        method: "get",
        url: "/base/getWeeklySignIn"
      })
        .then(
          function(response) {
            response.data.data.forEach(item => {
              this.signInDate.push(item.createTime.substring(0, 10));
              this.signInTotal.push(item.total);
            });
          }.bind(this)
        )
        .catch(function(error) {
          alert(error);
        });
    }
  },
  mounted() {
    this.getData();
    this.getData2();
    (this.orgOptions2 = {
      xAxis: {
        type: "category",
        data: [
          "Jan",
          "Feb",
          "Mar",
          "Apr",
          "May",
          "Jun",
          "Jul",
          "Aug",
          "Sept",
          "Oct",
          "Nov",
          "Dec"
        ]
      },
      yAxis: {
        type: "value"
      },
      series: [
        {
          data: this.monthlyArticlesList,
          type: "line",
          smooth: true
        }
      ]
    }),
      (this.orgOptions = {
        xAxis: {
          type: "category",
          data: this.signInDate
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            data: this.signInTotal,
            type: "line",
            smooth: true
          }
        ]
      });
  }
};
</script>
