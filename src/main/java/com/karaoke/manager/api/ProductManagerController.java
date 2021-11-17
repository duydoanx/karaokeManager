package com.karaoke.manager.api;

import com.karaoke.manager.dto.ProductDTO;
import com.karaoke.manager.entity.Product;
import com.karaoke.manager.entity.support.ResponseApi;
import com.karaoke.manager.entity.support.ResponsePage;
import com.karaoke.manager.mapper.ProductMapper;
import com.karaoke.manager.service.ProductService;
import com.karaoke.manager.validation.group.product.CreateProduct;
import com.karaoke.manager.validation.group.product.UpdateProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/products-manager")
@RestController
@RequiredArgsConstructor
public class ProductManagerController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  // API thêm sản phẩm
  @PostMapping("/add")
  public ResponseApi<ProductDTO> addProduct(
      @RequestBody @Validated(CreateProduct.class) ProductDTO productDTO) {
    Product product = productMapper.productDTOToProduct(productDTO);
    product = productService.save(product);
    return new ResponseApi<>(HttpStatus.OK.value(), productMapper.productToProductDTO(product));
  }

  // API sửa sản phẩm
  @PostMapping("/update/{productId}")
  public ResponseApi<?> updateProduct(
      @PathVariable Long productId,
      @RequestBody @Validated(UpdateProduct.class) ProductDTO productDTO) {
    Optional<Product> productOptional = productService.findById(productId);
    if (!productOptional.isPresent()) {
      throw new RuntimeException("Unable to find product.");
    }
    Product product = productOptional.get();
    productMapper.updateProductFromProductDTO(productDTO, product);
    productService.save(product);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API vô hiệu hoá sản phẩm
  @GetMapping("/disable/{productId}")
  public ResponseApi<?> disableProduct(@PathVariable Long productId) {
    Optional<Product> productOptional = productService.findById(productId);
    if (!productOptional.isPresent()) {
      throw new RuntimeException("Unable to find product.");
    }
    Product product = productOptional.get();
    product.setStatus(Product.DISABLE);
    productService.save(product);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API kích hoạt lại sản phẩm đã bị vô hiệu hoá
  @GetMapping("/enable/{productId}")
  public ResponseApi<?> enableProduct(@PathVariable Long productId) {
    Optional<Product> productOptional = productService.findById(productId);
    if (!productOptional.isPresent()) {
      throw new RuntimeException("Unable to find product.");
    }
    Product product = productOptional.get();
    if (!product.getStatus().equals(Product.DISABLE)) {
      throw new RuntimeException("Product has not been disabled.");
    }
    product.setStatus(Product.ENABLE);
    productService.save(product);
    return new ResponseApi<>(HttpStatus.OK.value());
  }

  // API xem tất cả sản phẩm
  @GetMapping
  public ResponseApi<ResponsePage> getProducts(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
    Pageable pageable =
        PageRequest.of(
            page,
            size,
            sort.equals("ASC") ? Sort.by("id").ascending() : Sort.by("id").descending());
    Page<Product> products = productService.findAll(pageable);
    return new ResponseApi<>(
        HttpStatus.OK.value(),
        new ResponsePage(
            products.getContent().stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList()),
            page,
            products.getTotalPages()));
  }

  // API xem từng sản phẩm theo số mã sản phẩm
  @GetMapping("/{productId}")
  public ResponseApi<ProductDTO> getProduct(@PathVariable Long productId) {
    Optional<Product> productOptional = productService.findById(productId);
    if (!productOptional.isPresent()) {
      throw new RuntimeException("Unable to find product.");
    }
    return new ResponseApi<>(
        HttpStatus.OK.value(), productMapper.productToProductDTO(productOptional.get()));
  }
}
