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
    console.log(this.value)
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
          _this.datalist.splice(index, 1)
        },
        onCancel () {}
      })
      this.$emit('change', this.datalist)
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
