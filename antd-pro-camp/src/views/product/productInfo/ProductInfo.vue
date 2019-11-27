<template>
  <div class="app-container">
    <a-card class="operate-container" :bordered="false">
      <a-icon type="bars" />
      <span>数据列表</span>
      <a-button
        style="float: right;"
        @click="handleAddProduct"
        size="small">
        添加
      </a-button>
    </a-card>
    <div class="table-container">
      <a-table
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="data"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template slot="prodImg" slot-scope="text">
          <img style="width: 80px; height: 80px;" :src="text" />
        </template>
        <span slot="action" slot-scope="text, record">
          <a-button
            size="small"
            type="primary"
            @click="handleShowProduct(record.id)">查看
          </a-button>
          <a-divider type="vertical" />
          <a-button
            size="small"
            type="primary"
            @click="handleEditProduct(record.id)">修改
          </a-button>
          <a-divider type="vertical" />
          <a-dropdown>
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="handleDeleteProduct(record.id)"><a-icon type="delete" />删除</a-menu-item>
            </a-menu>
            <a-button
              size="small"
              type="danger"
            >
              更多 <a-icon type="down" />
            </a-button>
          </a-dropdown>
        </span>
      </a-table>
    </div>
  </div>
</template>

<script>
import { getProductList, removeProductById } from '@/api/data.js'
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    // sorter: true,
    width: '20%'
  },
  {
    title: '产品图片',
    dataIndex: 'prodImg',
    scopedSlots: { customRender: 'prodImg' }
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime'
    // scopedSlots: { customRender: 'createTime' }
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]

export default {
  name: 'ProductInfo',
  mounted () {
    this.fetch({
      page: 1,
      size: 10
    })
  },
  data () {
    return {
      data: [],
      pagination: {},
      loading: false,
      columns
    }
  },
  methods: {
    handleAddProduct () {
      this.$router.push({ path: '/product/ProductAdd' })
    },
    // handleTableChange (pagination, filters, sorter) {
    handleTableChange (pagination, filters, sorter) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      this.pagination = pager
      this.fetch({
        keywords: '',
        page: pagination.current,
        size: pagination.pageSize
        // sortField: sorter.field,
        // sortOrder: sorter.order,
        // ...filters
      })
    },
    handleShowProduct (id) {
      this.$router.push({ path: '/product/productShow', query: { id: id } })
    },
    handleEditProduct (id) {
      this.$router.push({ path: '/product/productEdit', query: { id: id } })
    },
    // 获取产品oss的url数组并删除的操作应该放到后端应用服务器去做，所以注释掉了部分代码
    async handleDeleteProduct (id) {
      // 获取所有待删除产品中的对象url数组保存到finalArray中
      // const currentItem = await getCurrentProduct(id)
      // let finalArray = [currentItem.favicon, currentItem.prodImg]
      // if (currentItem.exampleList.length > 0) {
      //   currentItem.exampleList.forEach(element => {
      //     finalArray = [...finalArray, ...getImgUrlArray(element.htmlContent)]
      //   })
      // }
      // if (currentItem.courseList.length > 0) {
      //   const videoUrlArray = currentItem.courseList.map(element => element.videoTrail)
      //   finalArray = [...finalArray, ...videoUrlArray]
      // }
      // // console.log(finalArray)
      // // 筛选oss的url前缀
      // const ossUrlArray = filterS3UrlArray(s3Url, finalArray)
      // 发起产品删除请求
      await removeProductById(id)
        .then(resp => {
          if (resp.status === 'success') {
            this.$message.success(resp.msg, 2)
          } else {
            this.$message.warning(resp.msg, 2)
          }
        })
      // 通过removeFiles批量删除
      // removeFiles(ossUrlArray)
      //   .then(resp => {
      //     if (resp.status === 'success') {
      //       this.$message.success(resp.msg, 2)
      //     } else if (resp.failedFiles) {
      //       this.$message.warning(resp.msg, 2)
      //       console.log(resp.msg)
      //       console.log(resp.failedFiles)
      //     } else {
      //       this.$message.warning(resp.msg, 2)
      //     }
      //   })
      //   .catch(() => {
      //     this.$message.warning('服务器异常~', 2)
      //   })
      // 从前端产品列表中去掉该删除项,刷新
      this.fetch({
        keywords: '',
        page: this.pagination.current,
        size: this.pagination.pageSize
      })
    },
    fetch (params = {}) {
      const page = params.page
      const size = params.size
      this.loading = true
      getProductList({
        keywords: '',
        page: page,
        size: size
      }).then(res => {
        if (res.status === 'success') {
          const pagination = { ...this.pagination }
          // Read total count from server
          pagination.total = res.totalCount
          this.pagination = pagination
          this.data = res.data
        }
        this.loading = false
      })
    }
  }
}
</script>
