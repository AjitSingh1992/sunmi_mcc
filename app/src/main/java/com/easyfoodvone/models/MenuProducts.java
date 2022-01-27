package com.easyfoodvone.models;

import java.io.Serializable;
import java.util.List;

public class MenuProducts implements Serializable
{

    boolean success;
    String message;
    List<Data> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data
    {
        String product_id;
        String product_name;
        String product_price;
        String price;
        String quantity;
        List<SubProducts> product_sizes;
        boolean is_checked;

        public Data(String product_id, String product_name, String product_price, String price, String quantity, List<SubProducts> product_sizes, boolean is_checked) {
            this.product_id = product_id;
            this.product_name = product_name;
            this.product_price = product_price;
            this.price = price;
            this.quantity = quantity;
            this.product_sizes = product_sizes;
            this.is_checked = is_checked;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public boolean getChecked() {
            return is_checked;
        }

        public void setChecked(boolean checked) {
            is_checked = checked;
        }

        public List<SubProducts> getProduct_sizes() {
            return product_sizes;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public void setProduct_sizes(List<SubProducts> product_sizes) {
            this.product_sizes = product_sizes;
        }

        public class SubProducts implements Serializable
        {
            String size_id;
            String size_name;
            String sell_price;
            String quantity;
            String price;
            boolean is_checked;

            public boolean getChecked() {
                return is_checked;
            }

            public void setChecked(boolean checked) {
                is_checked = checked;
            }

            public String getSize_id() {
                return size_id;
            }

            public void setSize_id(String size_id) {
                this.size_id = size_id;
            }

            public String getSize_name() {
                return size_name;
            }

            public void setSize_name(String size_name) {
                this.size_name = size_name;
            }

            public String getSell_price() {
                return sell_price;
            }

            public void setSell_price(String sell_price) {
                this.sell_price = sell_price;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            @Override
            public String toString() {
                return "SubProducts{" +
                        "size_id='" + size_id + '\'' +
                        ", size_name='" + size_name + '\'' +
                        ", sell_price='" + sell_price + '\'' +
                        ", quantity='" + quantity + '\'' +
                        ", price='" + price + '\'' +
                        ", is_checked=" + is_checked +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "product_id='" + product_id + '\'' +
                    ", product_name='" + product_name + '\'' +
                    ", product_price='" + product_price + '\'' +
                    ", price='" + price + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", product_sizes=" + product_sizes +
                    ", is_checked=" + is_checked +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "MenuProducts{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
