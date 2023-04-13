package com.example.Shop.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Shop.Controller.dto.Constant;
import com.example.Shop.entities.ProductsEntity;
import com.example.Shop.entities.ProductsImagesEntity;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.service.IProductService;



@Service
public class ProductService implements IProductService,Constant {
	
	@Autowired
	ProductRepository productRepo;

	public void deleteAllById(Iterable<? extends Integer> integers) {
		productRepo.deleteAllById(integers);
	}

	@Override
	public <S extends ProductsEntity> S save(S entity) {
		return productRepo.save(entity);
	}

	@Override
	public Page<ProductsEntity> ProductShop(Pageable pageable) {
		return productRepo.ProductShop(pageable);
	}

	@Override
	public List<ProductsEntity> findProduct() {
		return productRepo.findProduct();
	}

	@Override
	public List<ProductsEntity> findProductShop() {
		return productRepo.findProductShop();
	}

	@Override
	public Page<ProductsEntity> findByKeywork(String keywork, Pageable pageable) {
		return productRepo.findByKeywork(keywork, pageable);
	}

	@Override
	public Page<ProductsEntity> findAll(String keywork, Pageable pageable) {
		return productRepo.findAll(keywork, pageable);
	}

	@Override
	public List<ProductsEntity> findAll() {
		return productRepo.findAll();
	}

	@Override
	public Page<ProductsEntity> findByTitle(String title, Pageable pageable) {
		return productRepo.findByTitle(title, pageable);
	}

	@Override
	public Page<ProductsEntity> findAll(Pageable pageable) {
		return productRepo.findAll(pageable);
	}

	@Override
	public List<ProductsEntity> findAll(Sort sort) {
		return productRepo.findAll(sort);
	}

	@Override
	public List<ProductsEntity> findAllById(Iterable<Integer> ids) {
		return productRepo.findAllById(ids);
	}
	@Override
	public ProductsEntity findById2(int id) {
		// TODO Auto-generated method stub
		return productRepo.findById(id).get();
	}
	@Override
	public Optional<ProductsEntity> findById(Integer id) {
		return productRepo.findById(id);
	}

	@Override
	public <S extends ProductsEntity> List<S> saveAll(Iterable<S> entities) {
		return productRepo.saveAll(entities);
	}

	@Override
	public <S extends ProductsEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
		return productRepo.findAll(example, pageable);
	}

	@Override
	public void deleteById(Integer id) {
		productRepo.deleteById(id);
	}

	@Override
	public void delete(ProductsEntity entity) {
		productRepo.delete(entity);
	}

	@Override
	public void deleteAll() {
		productRepo.deleteAll();
	}

	@Override
	public ProductsEntity getById(Integer id) {
		return productRepo.getById(id);
	}

	@Override
	public <S extends ProductsEntity> List<S> findAll(Example<S> example, Sort sort) {
		return productRepo.findAll(example, sort);
	}
	
	private boolean isEmptyUploadFile(MultipartFile[] images) {
		if (images == null || images.length <= 0)
			return true;

		if (images.length == 1 && images[0].getOriginalFilename().isEmpty())
			return true;

		return false;
	}

	private boolean isEmptyUploadFile(MultipartFile image) {
		return image == null || image.getOriginalFilename().isEmpty();
	}

	@Override
	public ProductsEntity addProduct(ProductsEntity product,MultipartFile inputAvatar, MultipartFile[] inputPictures) throws Exception {
		if(!isEmptyUploadFile(inputAvatar)) {
			String pathToFile = UPLOAD_FILE_ROOT + "/product/avatar/"
					
				+ inputAvatar.getOriginalFilename();
			inputAvatar.transferTo(new File(pathToFile));
			product.setAvatar("product/avatar/" +inputAvatar.getOriginalFilename());
		}
		
		//product imgages
		// có đẩy pictures ???
		if (!isEmptyUploadFile(inputPictures)) {
			String pathToFile = UPLOAD_FILE_ROOT +"/product/pictures/";
					
			for (MultipartFile pic : inputPictures) {
				pic.transferTo(new File(pathToFile + pic.getOriginalFilename()));

				ProductsImagesEntity pi = new ProductsImagesEntity();
				pi.setPath("product/pictures/" + pic.getOriginalFilename());
				pi.setTitle(pic.getOriginalFilename());

				product.addRelationProduct(pi);
			}
		}
		return productRepo.save(product);
	}
	public ProductsEntity editProduct(ProductsEntity product,MultipartFile productAvatar, MultipartFile[] productImages) throws Exception {
		//Có đẩy avatar lên không
				// lay thong tin san pham trong db.
				ProductsEntity productOnDb = productRepo.findById(product.getId()).get();
						
				// có đẩy avartar ???
				if(!isEmptyUploadFile(productAvatar)) {
					// xóa avatar trong folder lên
					new File(UPLOAD_FILE_ROOT + productOnDb.getAvatar()).delete();
							
					// add avartar moi
					productAvatar.transferTo(new File(UPLOAD_FILE_ROOT + "\\product\\avatar\\" + productAvatar.getOriginalFilename()));
					product.setAvatar("/product/avatar/" + productAvatar.getOriginalFilename());
				} else {
					// su dung lai avatar cu
					product.setAvatar(productOnDb.getAvatar());
				}
						
						// có đẩy pictures ???
				if (!isEmptyUploadFile(productImages)) {
							
							
					for (MultipartFile pic : productImages) {
						pic.transferTo(new File(UPLOAD_FILE_ROOT + "\\product\\pictures\\" + pic.getOriginalFilename()));

						ProductsImagesEntity pi = new ProductsImagesEntity();
						pi.setPath("/product/pictures/" + pic.getOriginalFilename());
						pi.setTitle(pic.getOriginalFilename());
								
						product.addRelationProduct(pi);
					}
				}
				
				return productRepo.save(product);
	}

	@Override
	public List<ProductsEntity> findByKeyword(String keywork) {
		// TODO Auto-generated method stub
		return productRepo.findByKeyword(keywork );
	}
	
	
}
