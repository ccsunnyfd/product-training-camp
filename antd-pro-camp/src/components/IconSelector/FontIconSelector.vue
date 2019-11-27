<template>
  <div :class="prefixCls">
    <a-tabs v-model="currentTab" @change="handleTabChange">
      <a-tab-pane v-for="v in fontIcons" :tab="v.title" :key="v.key">
        <ul>
          <li v-for="(icon, key) in v.icons" :key="`${v.key}-${key}`" :class="{ 'active': selectedIcon==icon }" @click="handleSelectedIcon(icon)" >
            <!-- <a-icon :type="icon" :style="{ fontSize: '36px' }" /> -->
            <icon-font :type="icon" :style="{ fontSize: '36px' }" />
          </li>
        </ul>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import fontIcons from './font-icons'
import IconFont from '@/components/IconFont.js'

export default {
  name: 'FontIconSelect',
  components: {
    IconFont
  },
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-icon-selector'
    },
    // eslint-disable-next-line
    value: {
      type: String
    }
  },
  data () {
    return {
      selectedIcon: this.value || '',
      currentTab: 'cat1',
      fontIcons
    }
  },
  watch: {
    value (val) {
      this.selectedIcon = val
      this.autoSwitchTab()
    }
  },
  created () {
    if (this.value) {
      this.autoSwitchTab()
    }
  },
  methods: {
    handleSelectedIcon (icon) {
      this.selectedIcon = icon
      this.$emit('change', icon)
    },
    handleTabChange (activeKey) {
      this.currentTab = activeKey
    },
    autoSwitchTab () {
      fontIcons.some(item => item.icons.some(icon => icon === this.value) && (this.currentTab = item.key))
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../index.less";

  ul{
    list-style: none;
    padding: 0;
    overflow-y: scroll;
    height: 250px;

    li{
      display: inline-block;
      padding: @padding-sm;
      margin: 3px 0;
      border-radius: @border-radius-base;

      &:hover, &.active{
        // box-shadow: 0px 0px 5px 2px @primary-color;
        cursor: pointer;
        color: @white;
        background-color: @primary-color;
      }
    }
  }
</style>
