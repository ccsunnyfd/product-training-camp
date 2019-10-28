import Mock from 'mockjs2'

const uploadStream = {
  status: 'success',
  msg: '上传form-data成功！',
  url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png'
}

const removeObject = {
  status: 'success',
  msg: '删除s3资源成功！'
}

const productAddForm = {
  status: 'success',
  msg: '添加新产品成功！'
}

Mock.mock(/\/api\/uploadStream/, 'post', uploadStream)
Mock.mock(/\/api\/removeObject/, 'post', removeObject)
Mock.mock(/\/api\/product\/addForm/, 'post', productAddForm)
