package com.been.onlinestore.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import com.been.onlinestore.domain.constant.DeliveryStatus;
import com.been.onlinestore.enums.ErrorMessages;
import com.been.onlinestore.exception.CustomException;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Entity
public class Delivery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@ColumnDefault("'ACCEPT'")
	@Column(nullable = false, length = 20)
	private DeliveryStatus deliveryStatus;

	@Column(nullable = false)
	private int deliveryFee;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime deliveredAt;

	protected Delivery() {
	}

	private Delivery(DeliveryStatus deliveryStatus, int deliveryFee, LocalDateTime deliveredAt) {
		this.deliveryStatus = deliveryStatus;
		this.deliveryFee = deliveryFee;
		this.deliveredAt = deliveredAt;
	}

	public static Delivery of(DeliveryStatus deliveryStatus, int deliveryFee, LocalDateTime deliveredAt) {
		return new Delivery(deliveryStatus, deliveryFee, deliveredAt);
	}

	public void startPreparing() {
		if (!canStartPreparing()) {
			throw new CustomException(ErrorMessages.CANNOT_START_PREPARING);
		}
		deliveryStatus = DeliveryStatus.PREPARING;
	}

	public void startDelivery() {
		if (!canStartDelivery()) {
			throw new CustomException(ErrorMessages.CANNOT_START_DELIVERY);
		}
		deliveryStatus = DeliveryStatus.DELIVERING;
	}

	public void completeDelivery() {
		if (!canCompleteDelivery()) {
			throw new CustomException(ErrorMessages.CANNOT_COMPLETE_DELIVERY);
		}
		deliveryStatus = DeliveryStatus.COMPLETED;
		deliveredAt = LocalDateTime.now();
	}

	public boolean canCancel() {
		return deliveryStatus == DeliveryStatus.ACCEPT;
	}

	private boolean canStartPreparing() {
		return deliveryStatus == DeliveryStatus.ACCEPT;
	}

	private boolean canStartDelivery() {
		return deliveryStatus == DeliveryStatus.PREPARING;
	}

	private boolean canCompleteDelivery() {
		return deliveryStatus == DeliveryStatus.DELIVERING;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Delivery that)) {
			return false;
		}
		return this.getId() != null && Objects.equals(this.getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
