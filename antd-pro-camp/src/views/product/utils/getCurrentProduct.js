import { getProductById, getExampleByProdId, getCourseByProdId } from '@/api/data.js'

const getCurrentProduct = function (id) {
  // const basicInfo = this.data.filter(prod => prod.id === id)[0]
  return Promise.all([
    // 获取基础数据
    getProductById({
      prodId: id
    }),
    // 获取应用案例数据
    getExampleByProdId({
      prodId: id
    }),
    // 获取课程数据
    getCourseByProdId({
      prodId: id
    })
  ])
}

export default getCurrentProduct
