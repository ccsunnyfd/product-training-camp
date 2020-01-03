<template>
  <div>
    <a-button size="small" @click="addOpt()">添加 +</a-button>
    <div v-for="(o, index) in datalist" :key="o.id">
      <a-input @change="handleTextChange($event, index)" :defaultValue="o.text">
        <span slot="addonBefore">{{ index+1 }}:</span>
        <a-button slot="addonAfter" size="small" @click="delOpt(index)">-</a-button>
      </a-Input>
      <a-checkbox v-model="o.right">正确答案</a-checkbox>
    </div>
  </div>
</template>
<script>
const UUID = require('uuid')

export default {
  name: 'OptionList',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    value: {
      type: Array
    }
  },
  computed: {
    datalist () {
      console.log(this.value)
      return this.value
    }
  },
  data () {
    return {
    }
  },
  methods: {
    // option选项文本变化
    handleTextChange (e, index) {
      Object.assign(this.datalist[index], { text: e.target.value })
      this.$emit('change', this.datalist)
    },
    // 添加一个option选项
    addOpt () {
      if (this.datalist && this.datalist.length > 0) {
        this.datalist.splice(this.datalist.length, 0, { _id: UUID.v1(), text: '' })
        this.$emit('change', this.datalist)
      } else {
        const newArr = [ { _id: UUID.v1(), text: '' } ]
        this.$emit('change', newArr)
      }
    },
    // 删除一个option选项
    delOpt (index) {
      this.datalist.splice(index, 1)
      this.$emit('change', this.datalist)
    }
  }
}
</script>

<style lang="less" scoped>
</style>
