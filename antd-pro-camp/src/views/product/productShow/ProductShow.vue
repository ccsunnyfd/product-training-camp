<template>
  <div class="app-container">
    <a-card class="basicInfo-container" :bordered="false">
      <span>基础数据</span>
      <div class="basicInfo-wrapper">
        <span>{{ currentItem.name }}</span>
        <a-divider type="vertical" />
        <span>{{ currentItem.description }}</span>
        <a-divider type="horizontal" />
        <span>场景和人群：</span>
        <div style="text-align: center">{{ currentItem.scenario }}</div>
        <a-divider type="horizontal" />
        <span>菜单小图标</span>
        <div style="text-align: center"><icon-font :type="currentItem.iconType" /></div>
        <a-divider type="horizontal" />
        <span>首页图标</span>
        <div style="text-align: center"><img style="width: 80px; height: 80px;" :src="currentItem.favicon" /></div>
        <a-divider type="horizontal" />
        <span>产品详情图片</span>
        <div style="text-align: center"><img style="width: 180px; height: 180px;" :src="currentItem.prodImg" /></div>
      </div>

    </a-card>
    <a-card class="exampleList-container" :bordered="false">
      <span>案例应用</span>
      <a-card v-for="example in currentItem.exampleList" :key="example.id">
        <span>标题: </span>
        <span>{{ example.title }}</span>
        <div v-html="example.htmlContent"/>
      </a-card>
    </a-card>
    <a-card class="courseList-container" :bordered="false">
      <span>课程信息</span>
      <a-card v-for="course in currentItem.courseList" :key="course.id">
        <span>序号: </span>
        <span>{{ course.chapterNum }}</span>
        <a-divider type="vertical" />
        <span>{{ course.title }}</span>
      </a-card>
    </a-card>
  </div>
</template>

<script>
import getCurrentProduct from '@/views/product/utils/getCurrentProduct'
import IconFont from '@/components/IconFont'

export default {
  name: 'ProductShow',
  components: {
    IconFont
  },
  data () {
    return {
      currentItem: {}
    }
  },
  mounted () {
    this.loading = true
    this.getProductDetail()
    this.loading = false
  },
  methods: {
    // getProductDetail
    getProductDetail () {
      const id = this.$route.query.id
      getCurrentProduct(id).then((results) => {
        const basicInfo = results[0].data
        const exampleList = results[1].data
        const courseList = results[2].data
        this.currentItem = { ...basicInfo, ...{ exampleList }, ...{ courseList } }
      })
    }
  }
}
</script>

<style lang="less" scoped>
span {
  font-weight: bold;
}
</style>
