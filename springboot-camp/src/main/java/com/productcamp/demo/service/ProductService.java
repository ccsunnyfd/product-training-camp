package com.productcamp.demo.service;

import com.productcamp.demo.model.Product;
import com.productcamp.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * ProductService
 *
 * @version 1.0
 */
@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProduct() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            return null;
        }
    }

    @Transactional
    public Long addNewProduct(Product product) {
        Product newProduct = productRepository.save(product);
        return newProduct.getId();
    }


    // 注意，PageRequest.of的第一个参数表示第几页，从0开始计数，这与通常的分页从1开始有些不同，要处理下
    //动态构造查询语句
    public Page<Product> getPageProducts(String keywords, Integer pageNum, Integer
            size) {
        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (!StringUtils.isEmpty(keywords)) {
                    Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + keywords + "%");
                    Predicate p2 = criteriaBuilder.like(root.get("description"), "%" + keywords + "%");
                    predicate = criteriaBuilder.or(p1, p2);
                }
                return predicate;
            }
        };
        Page<Product> page = productRepository.findAll(querySpeci, PageRequest.of(pageNum - 1, size));
        return page;
    }

}
