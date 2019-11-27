<template>
  <a-collapse accordion>
    <a-collapse-panel v-for="(item, index) in datalist" :key="item.key">
      <div slot="header">
        <span class="example-title">{{ item.title }}</span>
        <span class="example-button">
          <a-button size="small" type="danger" @click.stop="handleDelete(index)">删除</a-button>
        </span>
      </div>
      <example-list-content :htmlContent="item.htmlContent" />
    </a-collapse-panel>
  </a-collapse>
  </div>

</template>
<script>
import { s3Url, removeFiles } from '@/api/data.js'
import ExampleListContent from '@/components/Example/ExampleListContent'
export default {
  name: 'Step2',
  components: {
    ExampleListContent
  },
  props: {
    // eslint-disable-next-line vue/require-default-prop
    value: {
      type: Array
    }
  },
  computed: {
    datalist () {
      return this.value
    }
  },
  mounted () {
    // console.log(this.value)
  },
  data () {
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 24 }, sm: { span: 24 } }
    }
  },
  methods: {
    handleDelete (index) {
      const _this = this
      this.$confirm({
        content: '确实要删除这个案例吗?',
        onOk () {
          // const index = _this.datalist.findIndex(item => item.key === key)
          // 获取案例条中的图片地址数组
          const imgUrls = _this.getImgUrls(index)
          if (imgUrls.length > 0) {
            // 从oss中删除所有案例条中的图片
            _this.removeImgRequest(imgUrls)
          }
          _this.datalist.splice(index, 1)
        },
        onCancel () {}
      })
      this.$emit('change', this.datalist)
    },
    removeImgRequest (imgUrls) {
      // 过滤掉不是s3图片服务器地址的外域的url
      const s3Urlreg = new RegExp(s3Url)
      const serverUrlreg = /:\/\/(.*?)\/(.*)/
      // 过滤掉并发起图片删除请求
      const urlNeedDelete = imgUrls.reduce((finalList, url) => {
        if (s3Urlreg.test(url)) {
          finalList.push(serverUrlreg.exec(url)[2])
        }
        return finalList
      }, [])
      if (urlNeedDelete.length === 0) {
        return
      }
      removeFiles(urlNeedDelete)
        .then(resp => {
          if (resp.status === 'success') {
            this.$message.success(resp.msg, 2)
          } else if (resp.failedFiles) {
            this.$message.warning(resp.msg, 2)
            console.log(resp.msg)
            console.log(resp.failedFiles)
          } else {
            this.$message.warning(resp.msg, 2)
          }
        })
        .catch(() => {
          this.$message.warning('服务器异常~', 2)
        })
    },
    getImgUrls (index) {
      const reg = /<img\b.*?(?:>|\/>)/gi // 全局匹配<img >的子串
      const regSrc = /\bsrc\b\s*=\s*['"]?([^'"]*)['"]?/i // 匹配子串中的url部分
      const imgUrls = [] // 待删除案例条内的图片url
      const matchList = this.datalist[index].htmlContent.match(reg) // 全局匹配<img >的子串列表
      for (const i in matchList) {
        const srcStr = matchList[i].match(regSrc)
        if (srcStr.length > 0) {
          imgUrls.push(srcStr[1])
        }
      }
      return imgUrls
    }
  }
}
</script>

<style lang="less" scoped>
.stepFormText {
  margin-bottom: 24px;

  .ant-form-item-control {
    line-height: 22px;
  }
}
.example-title {
  overflow: hidden;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
  white-space:nowrap;
  width:300px;
  height:28px;
  display:inline-block;
}
.example-button {
  float: right;
  margin-right: 10px;
}
</style>
