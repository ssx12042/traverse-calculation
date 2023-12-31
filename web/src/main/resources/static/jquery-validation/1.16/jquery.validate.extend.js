(function (a) {
    a("#inputForm .box-footer [class*=col-sm-offset]").append('<div class="form-error">' + a.validator.messages.errorMessage + "</div>");
    a.extend(a.validator.defaults, {
        ignore: ":hidden:not(.required),input.select2-focusser",
        errorClass: "has-error",
        errorContainer: ".form-error",
        errorPlacement: function (b, c) {
            if (c.closest(".icheck").length > 0) {
                c = c.closest(".icheck");
                c.parent().css("position", "relative");
                b.insertAfter(c);
                b.css({top: c.position().top + c.outerHeight() + 2, left: c.position().left + 5});
                return
            }
            if (c.next().hasClass("select2")) {
                c = c.next();
                c.parent().css("position", "relative")
            } else {
                if (c.closest(".input-group").length > 0) {
                    c = c.closest(".input-group");
                    c.parent().css("position", "relative")
                }
            }
            b.insertAfter(c);
            b.css({top: c.position().top + c.outerHeight() - 5, left: c.position().left + 5})
        },
        highlight: function (b) {
            a(b).closest(".form-group").addClass("has-error")
        },
        unhighlight: function (b) {
            a(b).closest(".form-group").removeClass("has-error")
        },
        success: function (b) {
            b.remove()
        }
    });
    a.extend(a.fn, {
        resetValid: function () {
            var b = a(this), c;
            if (b.closest(".icheck").length > 0) {
                c = b.closest(".icheck").parent()
            } else {
                if (b.closest(".input-group").size() > 0) {
                    c = b.closest(".input-group").parent()
                } else {
                    c = b.parent()
                }
            }
            b.closest(".has-error").removeClass("has-error");
            c.find("label.has-error").remove()
        }
    })
}(jQuery));
jQuery.validator.addMethod("userName", function (b, a) {
    return this.optional(a) || /^[\u0391-\uFFE5\w]+$/.test(b)
}, $.validator.messages.userName);
jQuery.validator.addMethod("realName", function (b, a) {
    return this.optional(a) || /^[\u4e00-\u9fa5]{2,30}$/.test(b)
}, $.validator.messages.realName);
jQuery.validator.addMethod("abc", function (b, a) {
    return this.optional(a) || /^[a-zA-Z0-9_]*$/.test(b)
}, $.validator.messages.abc);
jQuery.validator.addMethod("noEqualTo", function (b, a, c) {
    return b != $(c).val()
}, $.validator.messages.noEqualTo);
jQuery.validator.addMethod("mobile", function (c, b) {
    var a = /^1[3,4,5,6,7,8,9]\d{9}$/g;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.mobile);
jQuery.validator.addMethod("simplePhone", function (c, b) {
    var a = /^(\d{3,4}-?)?\d{7,9}$/g;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.simplePhone);
jQuery.validator.addMethod("phone", function (c, b) {
    var a = /(^0[1-9]{1}\d{8,10}$)|(^1[3,4,5,6,7,8,9]\d{9}$)/g;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.phone);
jQuery.validator.addMethod("zipCode", function (c, b) {
    var a = /^[0-9]{6}$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.zipCode);
$.validator.addMethod("integer", function (b, a) {
    return this.optional(a) || /^-?\d+$/.test(b)
}, $.validator.messages.integer);
$.validator.addMethod("ipv4", function (b, a) {
    return this.optional(a) || /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/i.test(b)
}, $.validator.messages.ipv4);
$.validator.addMethod("ipv6", function (b, a) {
    return this.optional(a) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(b)
}, $.validator.messages.ipv6);
jQuery.validator.addMethod("qq", function (c, b) {
    var a = /^[1-9][0-9]{4,}$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.qq);
jQuery.validator.addMethod("idcard", function (c, a) {
    var b = {
        provinceAndCitys: {
            11: "北京",
            12: "天津",
            13: "河北",
            14: "山西",
            15: "内蒙古",
            21: "辽宁",
            22: "吉林",
            23: "黑龙江",
            31: "上海",
            32: "江苏",
            33: "浙江",
            34: "安徽",
            35: "福建",
            36: "江西",
            37: "山东",
            41: "河南",
            42: "湖北",
            43: "湖南",
            44: "广东",
            45: "广西",
            46: "海南",
            50: "重庆",
            51: "四川",
            52: "贵州",
            53: "云南",
            54: "西藏",
            61: "陕西",
            62: "甘肃",
            63: "青海",
            64: "宁夏",
            65: "新疆",
            71: "台湾",
            81: "香港",
            82: "澳门",
            91: "国外"
        },
        powers: ["7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"],
        parityBit: ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"],
        genders: {male: "男", female: "女"},
        checkAddressCode: function (e) {
            var d = /^[1-9]\d{5}$/.test(e);
            if (!d) {
                return false
            }
            if (b.provinceAndCitys[parseInt(e.substring(0, 2))]) {
                return true
            } else {
                return false
            }
        },
        checkBirthDayCode: function (i) {
            var f = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(i);
            if (!f) {
                return false
            }
            var h = parseInt(i.substring(0, 4), 10);
            var g = parseInt(i.substring(4, 6), 10);
            var d = parseInt(i.substring(6), 10);
            var e = new Date(h, g - 1, d);
            if (e > new Date()) {
                return false
            } else {
                if ((e.getFullYear() == h) && (e.getMonth() == g - 1) && (e.getDate() == d)) {
                    return true
                } else {
                    return false
                }
            }
        },
        getParityBit: function (g) {
            var h = g.substring(0, 17);
            var f = 0;
            for (var e = 0; e < 17; e++) {
                f += parseInt(h.charAt(e), 10) * parseInt(b.powers[e])
            }
            var d = f % 11;
            return b.parityBit[d]
        },
        checkParityBit: function (e) {
            var d = e.charAt(17).toUpperCase();
            if (b.getParityBit(e) == d) {
                return true
            } else {
                return false
            }
        },
        checkIdCardNo: function (e) {
            var d = /^\d{15}|(\d{17}(\d|x|X))$/.test(e);
            if (!d) {
                return false
            }
            if (e.length == 15) {
                return b.check15IdCardNo(e)
            } else {
                if (e.length == 18) {
                    return b.check18IdCardNo(e)
                } else {
                    return false
                }
            }
        },
        check15IdCardNo: function (f) {
            var d = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(f);
            if (!d) {
                return false
            }
            var e = f.substring(0, 6);
            d = b.checkAddressCode(e);
            if (!d) {
                return false
            }
            var g = "19" + f.substring(6, 12);
            return b.checkBirthDayCode(g)
        },
        check18IdCardNo: function (f) {
            var d = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(f);
            if (!d) {
                return false
            }
            var e = f.substring(0, 6);
            d = b.checkAddressCode(e);
            if (!d) {
                return false
            }
            var g = f.substring(6, 14);
            d = b.checkBirthDayCode(g);
            if (!d) {
                return false
            }
            return b.checkParityBit(f)
        },
        formateDateCN: function (e) {
            var g = e.substring(0, 4);
            var f = e.substring(4, 6);
            var d = e.substring(6);
            return g + "-" + f + "-" + d
        },
        getIdCardInfo: function (e) {
            var d = {gender: "", birthday: ""};
            if (e.length == 15) {
                var f = "19" + e.substring(6, 12);
                d.birthday = b.formateDateCN(f);
                if (parseInt(e.charAt(14)) % 2 == 0) {
                    d.gender = b.genders.female
                } else {
                    d.gender = b.genders.male
                }
            } else {
                if (e.length == 18) {
                    var f = e.substring(6, 14);
                    d.birthday = b.formateDateCN(f);
                    if (parseInt(e.charAt(16)) % 2 == 0) {
                        d.gender = b.genders.female
                    } else {
                        d.gender = b.genders.male
                    }
                }
            }
            return d
        },
        getId15: function (d) {
            if (d.length == 15) {
                return d
            } else {
                if (d.length == 18) {
                    return d.substring(0, 6) + d.substring(8, 17)
                } else {
                    return null
                }
            }
        },
        getId18: function (e) {
            if (e.length == 15) {
                var f = e.substring(0, 6) + "19" + e.substring(6);
                var d = b.getParityBit(f);
                return f + d
            } else {
                if (e.length == 18) {
                    return e
                } else {
                    return null
                }
            }
        }
    };
    return this.optional(a) || b.checkIdCardNo(c)
}, $.validator.messages.idcard);
// 2022年5月24日 su添加
// 垂直角的度
jQuery.validator.addMethod("integerVerticalDegree", function (c, b) {
    var a = /^-?(0?\d|[1-8]\d|90)$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.integerVerticalDegree);
// 角度的度
jQuery.validator.addMethod("integerDegree", function (c, b) {
    var a = /^(0?\d{1,2}|[1-2]\d{1,2}|3[0-5]\d)$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.integerDegree);
// 角度的分
jQuery.validator.addMethod("integerMinute", function (c, b) {
    var a = /^(0?\d|[1-5]\d)$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.integerMinute);
// 角度的秒
jQuery.validator.addMethod("doubleSecond", function (c, b) {
    var a = /^(0?\d(\.\d)?|[1-5]\d(\.\d)?)$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.doubleSecond);
// 平距
jQuery.validator.addMethod("double4decimal", function (c, b) {
    var a = /^(\d+(\.\d{1,4})?)$/;
    return this.optional(b) || (a.test(c))
}, $.validator.messages.double4decimal);