package com.been.onlinestore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.been.onlinestore.domain.Address;
import com.been.onlinestore.domain.User;
import com.been.onlinestore.enums.ErrorMessages;
import com.been.onlinestore.exception.CustomException;
import com.been.onlinestore.repository.AddressRepository;
import com.been.onlinestore.repository.UserRepository;
import com.been.onlinestore.service.dto.request.AddressServiceRequest;
import com.been.onlinestore.service.dto.response.AddressResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<AddressResponse> findAddresses(Long userId) {
		return addressRepository.findAllByUser_IdOrderByDefaultAddressDesc(userId).stream()
			.map(AddressResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public AddressResponse findAddress(Long addressId, Long userId) {
		return addressRepository.findByIdAndUser_Id(addressId, userId)
			.map(AddressResponse::from)
			.orElseThrow(() -> new CustomException(ErrorMessages.NOT_FOUND_ADDRESS));
	}

	public Long addAddress(Long userId, AddressServiceRequest serviceRequest) {
		Optional<Address> originalDefaultAddress = addressRepository.findDefaultAddressByUserId(userId);
		boolean isDefaultAddressRequest = serviceRequest.defaultAddress();

		if (originalDefaultAddress.isPresent()) {
			if (isDefaultAddressRequest) {
				originalDefaultAddress.get().updateDefaultAddress(false);
			}
		} else {
			isDefaultAddressRequest = true;
		}

		User user = userRepository.getReferenceById(userId);
		return addressRepository.save(serviceRequest.toEntity(user, isDefaultAddressRequest)).getId();
	}

	public Long updateAddress(Long addressId, Long userId, AddressServiceRequest serviceRequest) {
		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(() -> new CustomException(ErrorMessages.NOT_FOUND_ADDRESS));

		if (Boolean.TRUE.equals(serviceRequest.defaultAddress())) {
			addressRepository.findDefaultAddressByUserId(userId)
				.ifPresent(originalDefaultAddress -> originalDefaultAddress.updateDefaultAddress(false));
		}

		address.updateInfo(serviceRequest.detail(), serviceRequest.zipcode(), serviceRequest.defaultAddress());
		return addressId;
	}

	public Long deleteAddress(Long addressId, Long userId) {
		Address address = addressRepository.findByIdAndUser_Id(addressId, userId)
			.orElseThrow(() -> new CustomException(ErrorMessages.NOT_FOUND_ADDRESS));

		if (Boolean.TRUE.equals(address.getDefaultAddress())) {
			throw new CustomException(ErrorMessages.FAIL_TO_DELETE_DEFAULT_ADDRESS);
		}

		addressRepository.deleteById(addressId);
		return addressId;
	}
}
