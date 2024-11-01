package com.haruns.eticaretapp.service;

import com.haruns.eticaretapp.dto.request.AddProductRequestDto;
import com.haruns.eticaretapp.dto.request.ProductFilterDto;
import com.haruns.eticaretapp.dto.request.UpdateProductRequestDto;
import com.haruns.eticaretapp.entity.*;
import com.haruns.eticaretapp.entity.enums.ProductStatus;
import com.haruns.eticaretapp.exception.ErrorType;
import com.haruns.eticaretapp.exception.EticaretException;
import com.haruns.eticaretapp.repository.*;
import com.haruns.eticaretapp.utility.EntityIdOperator;
import com.haruns.eticaretapp.utility.ProductCodeGenerator;
import com.haruns.eticaretapp.utility.ProductSpecification;
import com.haruns.eticaretapp.view.VwProduct;
import com.haruns.eticaretapp.view.VwProductDisplay;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ComputerProductService implements MergedService<ComputerProduct>{
	private final ComputerProductRepository computerProductRepository;
	private final EntityIdOperator entityIdOperator;
	private final ProductSpecification<ComputerProduct> productSpecification;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final ProductImageRepository productImageRepository;
	private final ProductCommentRepository productCommentRepository;
	
	@Override
	public void addProduct(AddProductRequestDto dto, String sellerId) {
		if(dto.getComputerRam()==null || dto.getComputerCPU()==null || dto.getComputerGPU()==null || dto.getComputerMotherboard()==null || dto.getComputerScreenSize()==null)
			throw new EticaretException(ErrorType.MISSING_FIELDS);
		ComputerProduct computerProduct=ComputerProduct.builder()
		                                               .id(entityIdOperator.generateUniqueIdForProducts(dto.getProductType()))
		                                               .name(dto.getName())
		                                               .price(dto.getPrice())
		                                               .stock(dto.getStock())
		                                               .description(dto.getDescription())
		                                               .brand(dto.getBrand())
		                                               .type(dto.getProductType())
		                                               .categoryId(dto.getCategoryId())
		                                               .status(ProductStatus.PENDING)
		                                               .sellerId(sellerId)
		                                               .cpu(dto.getComputerCPU())
		                                               .gpu(dto.getComputerGPU())
		                                               .motherboard(dto.getComputerMotherboard())
		                                               .ram(dto.getComputerRam())
		                                               .screenSize(dto.getComputerScreenSize())
		                                               .build();
		computerProductRepository.save(computerProduct);
		computerProduct.setCode(ProductCodeGenerator.generateProductCode(computerProduct));
	}
	@Override
	public boolean existById(String id) {
		return computerProductRepository.existsById(id);
	}
	@Override
	public Optional<ComputerProduct> findById(String id) {
		return computerProductRepository.findById(id);
    }
	@Override
	public List<ComputerProduct> findAllByStatus(ProductStatus status) {
		return computerProductRepository.findAllByStatus(status);
	}
	@Override
	public void save(ComputerProduct product) {
		computerProductRepository.save(product);
	}
	@Override
	public void update(UpdateProductRequestDto dto) {
		ComputerProduct computerProduct = ComputerProduct.builder()
				.id(dto.id())
				.type(dto.productType())
				.name(dto.name())
		        .description(dto.description())
		        .brand(dto.brand())
				.price(dto.price())
				.stock(dto.stock())
				.cpu(dto.computerCPU())
				.gpu(dto.computerGPU())
				.screenSize(dto.computerScreenSize())
				.motherboard(dto.computerMotherboard())
		        .ram(dto.computerRam())
				.build();
		computerProductRepository.save(computerProduct);
	}
	@Override
	public void deleteById(String id) {
        computerProductRepository.deleteById(id);
    }
	
	@Override
	public List<ComputerProduct> filterProducts(ProductFilterDto filterDto){
		Specification<ComputerProduct> specification = productSpecification.getProductsByFilter(filterDto);
		return computerProductRepository.findAll(specification);
	}
	
		@Override
		public List<VwProduct> getTop10ByStatus(){
			LinkedList<VwProductDisplay> neededFields = computerProductRepository.getNeededFields(Pageable.ofSize(10));
			Map<String,String> storeIdNames = new HashMap<>();
			for (VwProductDisplay vwProduct : neededFields) {
				storeIdNames.put(vwProduct.getSellerId(),
				                 userRepository.findStoreNameById(vwProduct.getSellerId()));
			}
			Map<String,String> categoryIdNames = new HashMap<>();
			for (VwProductDisplay vwProduct : neededFields) {
				categoryIdNames.put(vwProduct.getCategoryId(),
				                    categoryRepository.findNameById(vwProduct.getCategoryId()));
			}
			
			Map<String,List<String>> productIdUrls=new HashMap<>();
			for (VwProductDisplay vwProduct : neededFields) {
				productIdUrls.put(vwProduct.getId(), productImageRepository.findUrlByProductId(vwProduct.getId()));
			}
			Map<String,List<ProductComment>> productIdComments=new HashMap<>();
			for (VwProductDisplay vwProduct : neededFields) {
				productIdComments.put(vwProduct.getId(), productCommentRepository.findCommentsByProductId(vwProduct.getId()));
			}
			List<VwProduct> computerProductViews = new ArrayList<>();
			for (VwProductDisplay vwProductDisplay : neededFields) {
				VwProduct vwProduct = VwProduct.builder()
				                               .productDisplay(vwProductDisplay)
				                               .storeName(storeIdNames.get(vwProductDisplay.getSellerId()))
				                               .categoryName(categoryIdNames.get(vwProductDisplay.getCategoryId()))
				                               .productUrls(productIdUrls.get(vwProductDisplay.getId()))
				                               .commentList(productIdComments.get(vwProductDisplay.getId()))
						.computerCPU(computerProductRepository.findCpuById(vwProductDisplay.getId()))
						.computerMotherboard(computerProductRepository.findMotherboardById(vwProductDisplay.getId()))
						.computerGPU(computerProductRepository.findGpuById(vwProductDisplay.getId()))
						.computerScreenSize(computerProductRepository.findScreenById(vwProductDisplay.getId()))
				                               .build();
				computerProductViews.add(vwProduct);
			}
			return computerProductViews;
		}
	}