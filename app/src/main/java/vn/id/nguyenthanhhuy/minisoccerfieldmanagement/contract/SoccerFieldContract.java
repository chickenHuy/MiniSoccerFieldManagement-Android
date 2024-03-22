package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.contract;

import android.provider.BaseColumns;

public final class SoccerFieldContract {
    private SoccerFieldContract(){}
    public static class  FieldEntry implements BaseColumns{
        public static final String TABLE_NAME = "Field";
        public static final String COLUMN_NAME_FIELD_NAME = "name";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
        public static final String COLUMN_NAME_TYPE_FIELD = "typeField";
        public static final String COLUMN_NAME_DATE_OF_WEEK = "dateOfWeek";
        public static final String COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES = "unitPricePer30Minutes";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class CustomerEntry implements BaseColumns{
        public static final String TABLE_NAME = "Customer";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_MEMBERSHIP_ID = "memberShipId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_NAME_TOTAL_SPEND = "totalSpend";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class BookingEntry implements BaseColumns{
        public static final String TABLE_NAME = "Booking";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CUSTOMER_ID = "customerId";
        public static final String COLUMN_NAME_FIELD_ID = "fieldId";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_TIME_START = "timeStart";
        public static final String COLUMN_NAME_TIME_END = "timeEnd";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }
    public static class MemberShipEntry implements BaseColumns {
        public static final String TABLE_NAME = "MemberShip";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DISCOUNT_RATE = "discountRate";
        public static final String COLUMN_NAME_MINIMUM_SPEND_AMOUNT = "minimumSpendAmount";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class MatchEntry implements BaseColumns {
        public static final String TABLE_NAME = "Match";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_BOOKING_ID = "bookingId";
        public static final String COLUMN_NAME_CHECK_IN = "checkIn";
        public static final String COLUMN_NAME_CHECK_OUT = "checkOut";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "Transaction";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_SERVICE_USAGE_ID = "serviceUsageId";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_TOTAL_AMOUNT = "totalAmount";
        public static final String COLUMN_NAME_ADDITIONAL_FEE = "additionalFee";
        public static final String COLUMN_NAME_DISCOUNT_AMOUNT = "discountAmount";
        public static final String COLUMN_NAME_FINAL_AMOUNT = "finalAmount";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class ServiceEntry implements BaseColumns {
        public static final String TABLE_NAME = "Service";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_UNIT = "unit";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_SOLD = "sold";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class ServiceItemsEntry implements BaseColumns {
        public static final String TABLE_NAME = "ServiceItems";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SERVICE_USAGE_ID = "serviceUsageId";
        public static final String COLUMN_NAME_SERVICE_ID = "serviceId";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class ServiceUsageEntry implements BaseColumns {
        public static final String TABLE_NAME = "ServiceUsage";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_MATCH_ID = "matchId";
        public static final String COLUMN_NAME_CUSTOMER_ID = "customerId";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_DATE_OF_BIRTH = "dateOfBirth";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phoneNumber";
        public static final String COLUMN_NAME_USER_NAME = "userName";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ROLE = "role";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    public static class PriceListEntry implements BaseColumns {
        public static final String TABLE_NAME = "PriceList";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
        public static final String COLUMN_NAME_TYPE_FIELD = "typeField";
        public static final String COLUMN_NAME_DATE_OF_WEEK = "dateOfWeek";
        public static final String COLUMN_NAME_UNIT_PRICE_PER_30_MINUTES = "unitPricePer30Minutes";
        public static final String COLUMN_NAME_IS_DELETED = "isDeleted";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

}

