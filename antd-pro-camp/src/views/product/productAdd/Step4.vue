<template>
  <div>
    <a-form style="margin: 40px auto 0;">
      <result title="操作成功" :is-success="true" description="已添加新产品信息" style="max-width: 560px;">
        <div class="information">
        </div>
        <div slot="action">
          <a-button type="primary" @click="finish">继续新增产品</a-button>
          <a-button style="margin-left: 8px" @click="toProductDetail">查看已添加产品信息</a-button>
        </div>
      </result>
    </a-form>
  </div>
</template>

<script>
import { Result } from '@/components'

export default {
  name: 'Step4',
  components: {
    Result
  },
  data () {
    return {
      loading: false
    }
  },
  methods: {
    finish () {
      this.$emit('finish')
    },
    toProductDetail () {
      // this.$router.push('/list/table-list')
    }
  }
}
</script>
<style lang="less" scoped>
  .information {
    line-height: 22px;

    .ant-row:not(:last-child) {
      margin-bottom: 24px;
    }
  }
  .money {
    font-family: "Helvetica Neue",sans-serif;
    font-weight: 500;
    font-size: 20px;
    line-height: 14px;
  }
</style>
