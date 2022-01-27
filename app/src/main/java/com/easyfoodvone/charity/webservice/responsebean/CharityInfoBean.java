/*Created by Omnisttechhub Solution*/
package com.easyfoodvone.charity.webservice.responsebean;

import java.util.List;

public class CharityInfoBean {

    private boolean success;
    private String message;
    private MealDonatedBean meal_donated;
    private ErrorsBean errors;

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

    public MealDonatedBean getMeal_donated() {
        return meal_donated;
    }

    public void setMeal_donated(MealDonatedBean meal_donated) {
        this.meal_donated = meal_donated;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public static class MealDonatedBean {


        private String no_of_meals;
        private String meal_targeted;
        private List<PreviousMealsBean> previous_meals;

        public String getNo_of_meals() {
            return no_of_meals;
        }

        public void setNo_of_meals(String no_of_meals) {
            this.no_of_meals = no_of_meals;
        }

        public String getMeal_targeted() {
            return meal_targeted;
        }

        public void setMeal_targeted(String meal_targeted) {
            this.meal_targeted = meal_targeted;
        }

        public List<PreviousMealsBean> getPrevious_meals() {
            return previous_meals;
        }

        public void setPrevious_meals(List<PreviousMealsBean> previous_meals) {
            this.previous_meals = previous_meals;
        }

        public static class PreviousMealsBean {
            /**
             * id : 9b9c01c0-d9ff-11e9-b56e-0657952ed75a
             * restaurant_id : 2f1cbbd0-65b4-11e9-abf6-0657952ed75a
             * no_of_meals : 2
             * ready_to_collect : 0-1
             * is_collected : 0
             * created_at : 2019-09-18 11:32:26
             * updated_at : 2019-09-18 11:32:26
             * charity_meal_request : [{"id":"6f61fa5c-d517-11e9-8d3d-2f3d4bed7a5b","chartymeal_id_fk":"d9e39fee-d3ae-11e9-9eeb-394f18b11812","is_accepted":1,"charity_id":"2e1376da-d3a4-11e9-966c-3fc9cb638722","charity":{"id":"2e1376da-d3a4-11e9-966c-3fc9cb638722","name":"Helping Hands","contact_name":"Rav","charity_number":"9874563210","email":"helping@gmail.com","address":"London","contact_number":"8521479630","postcode":"B13 9PG","postcode_slug":"b13","contact_mobile":"8521479630","added_by":"f9f22c6a-4f06-11e9-a0c0-0657952ed75a","status":1,"deleted_at":null,"created_at":"2019-09-10 09:22:51","updated_at":"2019-09-10 09:23:19"}}]
             */

            private String id;
            private String restaurant_id;
            private String no_of_meals;
            private String ready_to_collect;
            private int is_collected;

            public int getIs_Accepted() {
                return is_Accepted;
            }

            public void setIs_Accepted(int is_Accepted) {
                this.is_Accepted = is_Accepted;
            }

            private int is_Accepted;
            private String created_at;
            private String updated_at;
            private List<CharityMealRequestBean> charity_meal_request;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRestaurant_id() {
                return restaurant_id;
            }

            public void setRestaurant_id(String restaurant_id) {
                this.restaurant_id = restaurant_id;
            }

            public String getNo_of_meals() {
                return no_of_meals;
            }

            public void setNo_of_meals(String no_of_meals) {
                this.no_of_meals = no_of_meals;
            }

            public String getReady_to_collect() {
                return ready_to_collect;
            }

            public void setReady_to_collect(String ready_to_collect) {
                this.ready_to_collect = ready_to_collect;
            }

            public int getIs_collected() {
                return is_collected;
            }

            public void setIs_collected(int is_collected) {
                this.is_collected = is_collected;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public List<CharityMealRequestBean> getCharity_meal_request() {
                return charity_meal_request;
            }

            public void setCharity_meal_request(List<CharityMealRequestBean> charity_meal_request) {
                this.charity_meal_request = charity_meal_request;
            }

            public static class CharityMealRequestBean {
                /**
                 * id : 6f61fa5c-d517-11e9-8d3d-2f3d4bed7a5b
                 * chartymeal_id_fk : d9e39fee-d3ae-11e9-9eeb-394f18b11812
                 * is_accepted : 1
                 * charity_id : 2e1376da-d3a4-11e9-966c-3fc9cb638722
                 * charity : {"id":"2e1376da-d3a4-11e9-966c-3fc9cb638722","name":"Helping Hands","contact_name":"Rav","charity_number":"9874563210","email":"helping@gmail.com","address":"London","contact_number":"8521479630","postcode":"B13 9PG","postcode_slug":"b13","contact_mobile":"8521479630","added_by":"f9f22c6a-4f06-11e9-a0c0-0657952ed75a","status":1,"deleted_at":null,"created_at":"2019-09-10 09:22:51","updated_at":"2019-09-10 09:23:19"}
                 */

                private String id;
                private String chartymeal_id_fk;
                private int is_accepted;
                private String charity_id;
                private CharityBean charity;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getChartymeal_id_fk() {
                    return chartymeal_id_fk;
                }

                public void setChartymeal_id_fk(String chartymeal_id_fk) {
                    this.chartymeal_id_fk = chartymeal_id_fk;
                }

                public int getIs_accepted() {
                    return is_accepted;
                }

                public void setIs_accepted(int is_accepted) {
                    this.is_accepted = is_accepted;
                }

                public String getCharity_id() {
                    return charity_id;
                }

                public void setCharity_id(String charity_id) {
                    this.charity_id = charity_id;
                }

                public CharityBean getCharity() {
                    return charity;
                }

                public void setCharity(CharityBean charity) {
                    this.charity = charity;
                }

                public static class CharityBean {
                    /**
                     * id : 2e1376da-d3a4-11e9-966c-3fc9cb638722
                     * name : Helping Hands
                     * contact_name : Rav
                     * charity_number : 9874563210
                     * email : helping@gmail.com
                     * address : London
                     * contact_number : 8521479630
                     * postcode : B13 9PG
                     * postcode_slug : b13
                     * contact_mobile : 8521479630
                     * added_by : f9f22c6a-4f06-11e9-a0c0-0657952ed75a
                     * status : 1
                     * deleted_at : null
                     * created_at : 2019-09-10 09:22:51
                     * updated_at : 2019-09-10 09:23:19
                     */

                    private String id;
                    private String name;
                    private String contact_name;
                    private String charity_number;
                    private String email;
                    private String address;
                    private String contact_number;
                    private String postcode;
                    private String postcode_slug;
                    private String contact_mobile;
                    private String added_by;
                    private int status;
                    private Object deleted_at;
                    private String created_at;
                    private String updated_at;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getContact_name() {
                        return contact_name;
                    }

                    public void setContact_name(String contact_name) {
                        this.contact_name = contact_name;
                    }

                    public String getCharity_number() {
                        return charity_number;
                    }

                    public void setCharity_number(String charity_number) {
                        this.charity_number = charity_number;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
                    }

                    public String getAddress() {
                        return address;
                    }

                    public void setAddress(String address) {
                        this.address = address;
                    }

                    public String getContact_number() {
                        return contact_number;
                    }

                    public void setContact_number(String contact_number) {
                        this.contact_number = contact_number;
                    }

                    public String getPostcode() {
                        return postcode;
                    }

                    public void setPostcode(String postcode) {
                        this.postcode = postcode;
                    }

                    public String getPostcode_slug() {
                        return postcode_slug;
                    }

                    public void setPostcode_slug(String postcode_slug) {
                        this.postcode_slug = postcode_slug;
                    }

                    public String getContact_mobile() {
                        return contact_mobile;
                    }

                    public void setContact_mobile(String contact_mobile) {
                        this.contact_mobile = contact_mobile;
                    }

                    public String getAdded_by() {
                        return added_by;
                    }

                    public void setAdded_by(String added_by) {
                        this.added_by = added_by;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public Object getDeleted_at() {
                        return deleted_at;
                    }

                    public void setDeleted_at(Object deleted_at) {
                        this.deleted_at = deleted_at;
                    }

                    public String getCreated_at() {
                        return created_at;
                    }

                    public void setCreated_at(String created_at) {
                        this.created_at = created_at;
                    }

                    public String getUpdated_at() {
                        return updated_at;
                    }

                    public void setUpdated_at(String updated_at) {
                        this.updated_at = updated_at;
                    }
                }
            }
        }
    }

    public static class ErrorsBean {
    }
}
