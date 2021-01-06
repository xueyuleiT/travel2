package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class CityBean {
    private List<ProvinceListBean> province_list;

    public List<ProvinceListBean> getProvince_list() {
        return province_list;
    }

    public void setProvince_list(List<ProvinceListBean> province_list) {
        this.province_list = province_list;
    }

    public static class ProvinceListBean {
        /**
         * province_id : 340000
         * province_name : 福建省
         * city_list : [{"city_id":350100,"city_name":"福州市","is_default":"Y"}]
         */

        private int province_id;
        private String province_name;
        private List<CityListBean> city_list;

        public int getProvince_id() {
            return province_id;
        }

        public void setProvince_id(int province_id) {
            this.province_id = province_id;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public List<CityListBean> getCity_list() {
            return city_list;
        }

        public void setCity_list(List<CityListBean> city_list) {
            this.city_list = city_list;
        }

        public static class CityListBean {
            /**
             * city_id : 350100
             * city_name : 福州市
             * is_default : Y
             */

            private int city_id;
            private String city_name;
            private String is_default;

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }
        }
    }
}
