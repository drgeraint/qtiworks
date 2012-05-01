/* Copyright (c) 2012, University of Edinburgh.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * * Neither the name of the University of Edinburgh nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * This software is derived from (and contains code from) QTItools and MathAssessEngine.
 * QTItools is (c) 2008, University of Southampton.
 * MathAssessEngine is (c) 2010, University of Edinburgh.
 */
package uk.ac.ed.ph.qtiworks.domain.entities;

import uk.ac.ed.ph.jqtiplus.node.AssessmentObjectType;
import uk.ac.ed.ph.jqtiplus.node.item.AssessmentItem;
import uk.ac.ed.ph.jqtiplus.node.test.AssessmentTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents an {@link AssessmentItem} or {@link AssessmentTest} stored within
 * the system. All such objects are treated as IMS Content Packages.
 *
 * @author David McKain
 */
@Entity
@Table(name="assessment_packages")
@SequenceGenerator(name="assessmentPackageSequence", sequenceName="assessment_package_sequence", initialValue=1, allocationSize=10)
public class AssessmentPackage implements BaseEntity, TimestampedOnCreation {

    private static final long serialVersionUID = -4330181851974184912L;

    @Id
    @GeneratedValue(generator="assessmentPackageSequence")
    @Column(name="aid")
    private Long aid;

    @Basic(optional=false)
    @Column(name="creation_time",updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    /** {@link InstructorUser} who uploaded this package */
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="owner_uid", updatable=false)
    private InstructorUser owner;

    /** Item or Test? */
    @Basic(optional=false)
    @Column(name="type", updatable=false, length=15)
    @Enumerated(EnumType.STRING)
    private AssessmentObjectType assessmentType;

    /** Content Package or standalone? */
    @Basic(optional=false)
    @Column(name="import_type", length=15)
    @Enumerated(EnumType.STRING)
    private AssessmentPackageImportType importType;

    /**
     * Title of this item/test. Used for listings and other stuff.
     * We take this from the QTI just after import.
     */
    @Basic(optional=false)
    @Column(name="title")
    private String title;

    /** Base path where this package's files belong. Treated as a sandbox */
    @Lob
    @Basic(optional=false)
    @Column(name="base_path")
    private String basePath;

    /** Href of the assessment item/test within this package */
    @Lob
    @Basic(optional=false)
    @Column(name="assessment_href")
    private String assessmentHref;

    /** Has this item/test been validated? */
    @Basic(optional=false)
    @Column(name="validated")
    private boolean validated;

    /** If validated, was this item/test found to be valid? */
    @Basic(optional=false)
    @Column(name="valid")
    private boolean valid;

    /**
     * Hrefs of all file resources declared within this package.
     * Used to determine what resources are allowed to be served up
     */
    @Lob
    @ElementCollection
    @CollectionTable(name="assessment_files", joinColumns=@JoinColumn(name="aid"))
    @Column(name="href")
    private Set<String> fileHrefs = new HashSet<String>();

    /**
     * All deliveries of this assessment
     *
     * FIXME: So far we're only allowing items to be delivered!
     */
    @OneToMany(fetch=FetchType.LAZY, mappedBy="assessmentPackage")
    @OrderColumn(name="order_index")
    private final List<AssessmentItemDelivery> assessmentDeliveries = new ArrayList<AssessmentItemDelivery>();

    //------------------------------------------------------------

    @Override
    public Long getId() {
        return aid;
    }

    @Override
    public void setId(final Long id) {
        this.aid = id;
    }


    @Override
    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }


    public InstructorUser getOwner() {
        return owner;
    }

    public void setOwner(final InstructorUser owner) {
        this.owner = owner;
    }


    public AssessmentObjectType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(final AssessmentObjectType assessmentType) {
        this.assessmentType = assessmentType;
    }


    public AssessmentPackageImportType getImportType() {
        return importType;
    }

    public void setImportType(final AssessmentPackageImportType importType) {
        this.importType = importType;
    }


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(final String basePath) {
        this.basePath = basePath;
    }


    public String getAssessmentHref() {
        return assessmentHref;
    }

    public void setAssessmentHref(final String assessmentHref) {
        this.assessmentHref = assessmentHref;
    }


    public boolean isValidated() {
        return validated;
    }

    public void setValidated(final boolean validated) {
        this.validated = validated;
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(final boolean valid) {
        this.valid = valid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }


    public List<AssessmentItemDelivery> getAssessmentDeliveries() {
        return assessmentDeliveries;
    }


    public Set<String> getFileHrefs() {
        return fileHrefs;
    }

    public void setFileHrefs(final Set<String> fileHrefs) {
        this.fileHrefs = fileHrefs;
    }

}