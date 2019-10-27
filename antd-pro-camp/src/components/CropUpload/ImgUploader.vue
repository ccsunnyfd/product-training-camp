<template>
  <div :style="{ minHeight: '180px' }">
    <div class="ant-upload-preview" @click="modal.edit(id)" >
      <a-icon type="cloud-upload-o" class="upload-icon"/>
      <div class="mask">
        <a-icon type="plus" />
      </div>
      <img :src="imgUrl" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImgUploader',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    value: String,
    // eslint-disable-next-line vue/require-default-prop
    modal: Object,
    id: {
      type: Number,
      required: true
    }
  },
  computed: {
    imgUrl () {
      return this.value === '' ? '/pleaseUpload.png' : this.value
    }
  },
  watch: {
    value (val) {
      this.$emit('change', val)
    }
  }
}
</script>

<style lang="less" scoped>
.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }
  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0,0,0,0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img, .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 0%;
    overflow: hidden;
  }
}
</style>
