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

	public boolean canStartPreparing() {
		return this.deliveryStatus == DeliveryStatus.ACCEPT;
	}

	public void startPreparing() {
		if (canStartPreparing()) {
			this.deliveryStatus = DeliveryStatus.PREPARING;
		}
	}

	public boolean canStartDelivery() {
		return this.deliveryStatus == DeliveryStatus.PREPARING;
	}

	public void startDelivery() {
		if (canStartDelivery()) {
			this.deliveryStatus = DeliveryStatus.DELIVERING;
		}
	}

	public boolean canCompleteDelivery() {
		return this.deliveryStatus == DeliveryStatus.DELIVERING;
	}

	public void completeDelivery() {
		if (canCompleteDelivery()) {
			this.deliveryStatus = DeliveryStatus.FINAL_DELIVERY;
		}
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
