<template>
  <div class="htmlContent">
    <slot>
      <p v-html="htmlContent"></p>
    </slot>
  </div>

</template>

<script>
export default {
  name: 'ExampleListContent',
  props: {
    htmlContent: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="less" scoped>
</style>
