<template>
  <div>
    <div>
      <a-button size="small" @click="addOpt()">添加 +</a-button>
    </div>
    <a-checkbox-group v-if="type === 3" :value="rightIds" @change="handleCheckChange">
      <div v-for="(o, index) in optionList" :key="o.id">
        <a-row>
          <a-col :span="18">
            <a-input @change="handleTextChange($event, index)" :value="o.text">
              <span slot="addonBefore">{{ "(" + String.fromCharCode('A'.charCodeAt() + index) + ") " }}:</span>
              <a-button slot="addonAfter" size="small" @click="delOpt(index)">-</a-button>
            </a-Input>
          </a-col>
          <a-col :offset="1" :span="5">
            <a-checkbox :value="o.id">
              答案
            </a-checkbox>
          </a-col>
        </a-row>
      </div>
    </a-checkbox-group>
    <a-radio-group v-else :value="rightIds[0]" @change="handleRadioChange">
      <div v-for="(o, index) in optionList" :key="o.id">
        <a-row>
          <a-col :span="18">
            <a-input @change="handleTextChange($event, index)" :value="o.text">
              <span slot="addonBefore">{{ "(" + String.fromCharCode('A'.charCodeAt() + index) + ") " }}:</span>
              <a-button slot="addonAfter" size="small" @click="delOpt(index)">-</a-button>
            </a-Input>
          </a-col>
          <a-col :offset="1" :span="5">
            <a-radio :value="o.id">
              答案
            </a-radio>
          </a-col>
        </a-row>
      </div>
    </a-radio-group>
  </div>
</template>

<script>
const UUID = require('uuid')

// const hasProp = (instance, prop) => {
//   const $options = instance.$options || {}
//   const propsData = $options.propsData || {}
//   return prop in propsData
// }

export default {
  name: 'OptionList',
  props: {
    type: {
      type: Number,
      default: 1
    },
    // eslint-disable-next-line vue/require-default-prop
    value: {
      type: Object
    }
  },
  data () {
    const value = this.value || {}
    return {
      optionList: value.optionList || [],
      rightIds: value.rightIds || []
    }
  },
  watch: {
    value (val = {}) {
      this.optionList = val.optionList || []
      this.rightIds = val.rightIds || []
    }
  },
  methods: {
    // option选项文本变化
    handleTextChange (e, index) {
      const inputContent = e.target.value || ''
      Object.assign(this.optionList[index], { text: inputContent })
      this.triggerChange({ optionList: this.optionList })
    },

    // 这里要对每一个选项的勾选状态重新置位，有些变成unchecked了
    // 多选：option选项是否为正确答案的标记发生改变
    handleCheckChange (checkedValues) {
      this.rightIds = checkedValues
      this.triggerChange({ rightIds: checkedValues })
    },
    // 单选：option选项是否为正确答案的标记发生改变
    handleRadioChange (e) {
      const index = e.target.value
      this.rightIds = [index]
      this.triggerChange({ rightIds: [index] })
    },
    triggerChange (changedValue) {
      this.$emit('change', Object.assign({}, this.$data, changedValue))
    },
    // 添加一个option选项
    addOpt () {
      if (this.optionList && this.optionList.length > 0) {
        this.optionList.splice(this.optionList.length, 0, { id: UUID.v1(), text: '' })
        this.triggerChange({ optionList: this.optionList })
      } else {
        this.optionList = [ { id: UUID.v1(), text: '' } ]
        this.triggerChange({ optionList: this.optionList })
      }
    },
    // 删除一个option选项
    delOpt (index) {
      this.optionList.splice(index, 1)
      this.triggerChange({ optionList: this.optionList })
    }
  }
}
</script>

<style lang="less" scoped>
</style>
