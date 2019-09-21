<template>
  <div>
    <div class="icon-box-wrapper">
      <i :class="`iconContent iconfont ${displayIcon}`" style="font-size: 30px;"></i>
    </div>
    <div style="margin-top:20px; text-align: center">
      <i-button shape="circle" icon="ios-search" @click="iconListVisible = true"></i-button>
      <span style="padding-left:10px; font-size:7px;">修改小图标</span>
    </div>
    <!-- iconFont 图标选择-->
    <Modal v-model="iconListVisible" width=800 :closable="false" :mask-closable="false">
      <p slot="header" style="text-align:center">
        <span style="font-size:18px;font-weight:500;color:#1a1a1a;">
          图标选择
        </span>
      </p>
      <div>
        <Row type="flex" justify="start">
          <i-col span="3" v-for="(item) in iconsForChosen" :key="item">
            <div class="icon-for-chosen-box-wrapper" style="margin-top: 10px;" @click="handleIconChosen(item)">
              <i :class="`iconContent iconfont ${item}`" style="font-size: 30px;"></i>
            </div>
          </i-col>
        </Row>
      </div>
      <div slot="footer" class="dialog-footer">
        <Button @click="iconListVisible = false">取 消</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  name: 'CchooseIcon',
  props: {
    iconinfo: {
      type: String,
      default: 'icon-bear'
    }
  },
  data () {
    return {
      iconsForChosen: [],
      iconListVisible: false
    }
  },
  computed: {
    displayIcon () {
      return this.iconinfo === '' ? 'icon-test_icon' : this.iconinfo
    }
  },
  methods: {
    handleIconChosen (iconType) {
      this.$emit('iconUpdate', iconType)
      this.iconListVisible = false
    }
  },
  mounted () {
    this.iconsForChosen = this.$config.iconsForChosen
  }
}
</script>

<style lang="less">
  @import './chooseIcon.less';
</style>
